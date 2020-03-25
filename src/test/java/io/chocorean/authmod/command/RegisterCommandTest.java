package io.chocorean.authmod.command;

import com.mojang.authlib.GameProfile;
import io.chocorean.authmod.core.DataSourceGuard;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.Player;
import io.chocorean.authmod.core.PlayerInterface;
import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.event.Handler;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterCommandTest {
  private Handler handler;
  private DataSourceStrategyInterface dataSource;
  private GuardInterface guard;
  private ServerPlayerEntity playerEntity;
  private CommandSource source;

  @BeforeEach
  void init() throws Exception {
    File file = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
    Files.deleteIfExists(file.toPath());
    this.handler = new Handler();
    PlayerInterface player = new Player("Batman", "7128022b-9195-490d-9bc8-9b42ebe2a8e3");
    this.playerEntity = mock(ServerPlayerEntity.class);

    when(this.playerEntity.getGameProfile()).thenReturn(new GameProfile(UUID.fromString(player.getUuid()), player.getUsername()));
    when(this.playerEntity.getDisplayName()).thenReturn(new StringTextComponent(player.getUsername()));
    this.source = mock(CommandSource.class);
    when(this.source.asPlayer()).thenReturn(this.playerEntity);
    this.dataSource = new FileDataSourceStrategy(file);
    this.guard = new DataSourceGuard(this.dataSource);
  }

  @Test
  void testExecute() {
    int res = RegisterCommand.execute(this.source, this.handler, this.guard, null, "adminroot", "adminroot");
    assertEquals(1, res);
    assertTrue(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteWrongPassword() {
    int res = RegisterCommand.execute(this.source, this.handler, this.guard, null, "wrongpass", "wrongpassword");
    assertEquals(1, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteWrongNumberOfArgs() {
    int res = RegisterCommand.execute(this.source, this.handler, this.guard, null, null, null);
    assertEquals(1, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteIdentifierRequired() {
    this.guard = new DataSourceGuard(this.dataSource, true);
    int res = RegisterCommand.execute(this.source, this.handler, this.guard, "elliotalderson@protonmail.ch", "MrRobot", "MrRobot");
    assertEquals(1, res);
    assertTrue(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteIdentifierMissing() {
    this.guard = new DataSourceGuard(this.dataSource, true);
    int res = RegisterCommand.execute(this.source, this.handler, this.guard, null, "MrRobot", "MrRobot");
    assertEquals(1, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteAlreadyLogged() {
    handler.authorizePlayer(playerEntity);
    assertTrue(this.handler.isLogged(this.playerEntity));
    int res = RegisterCommand.execute(this.source, this.handler, this.guard, null, "MrRobot", "MrRobot");
    assertEquals(1, res);
    assertTrue(this.handler.isLogged(this.playerEntity));
  }

}
