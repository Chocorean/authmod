package io.chocorean.authmod.command;

import com.mojang.authlib.GameProfile;
import io.chocorean.authmod.core.*;
import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.event.Handler;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
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
  private PlayerInterface player;
  private PayloadInterface payload;
  private ServerPlayerEntity playerEntity;
  private CommandSource source;

  @BeforeEach
  void init() throws Exception {
    File file = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
    Files.deleteIfExists(file.toPath());
    this.handler = new Handler();
    this.player = new Player("Batman", "7128022b-9195-490d-9bc8-9b42ebe2a8e3");
    this.playerEntity = mock(ServerPlayerEntity.class);
    this.payload = new Payload(player, new String[]{"adminroot", "adminroot"});
    when(this.playerEntity.getGameProfile()).thenReturn(new GameProfile(UUID.fromString(player.getUuid()), player.getUsername()));
    when(this.playerEntity.getDisplayName()).thenReturn(new StringTextComponent(player.getUsername()));
    this.source = mock(CommandSource.class);
    when(this.source.asPlayer()).thenReturn(this.playerEntity);
    this.dataSource = new FileDataSourceStrategy(file);
    this.guard = new DataSourceGuard(this.dataSource);
  }

  @Test
  void testExecute() {
    int res = RegisterCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertEquals(0, res);
    assertTrue(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteWrongPassword() {
    this.payload.getArgs()[0] = "wrongpassword";
    int res = RegisterCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertEquals(1, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteWrongNumberOfArgs() {
    this.payload = new Payload(this.player, new String[]{});
    int res = RegisterCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertEquals(1, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteIdentifierRequired() {
    this.payload = new Payload(this.player, new String[]{"elliotalderson@protonmail.ch", "MrRobot", "MrRobot"});
    this.guard = new DataSourceGuard(this.dataSource, true);
    int res = RegisterCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertEquals(0, res);
    assertTrue(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteIdentifierMissing() {
    this.guard = new DataSourceGuard(this.dataSource, true);
    int res = RegisterCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertEquals(1, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteAlreadyLogged() {
    handler.authorizePlayer(this.playerEntity);
    assertTrue(this.handler.isLogged(this.playerEntity));
    int res = RegisterCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertEquals(0, res);
    assertTrue(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testBugFix3_2() throws Exception {
    /*
     * Context:
     *     Strategy: FILE
     *     emailRequired: False
     * How to reproduce:
     *     register once person successfully
     *     try to register a second one
     * Expected message: You are already logged in.
     */
    // Init
    PlayerInterface secondPlayer = new Player("Superman", "094b0779-992c-4fac-90f5-fa839dc77dbc");
    ServerPlayerEntity secondPlayerEntity = mock(ServerPlayerEntity.class);
    when(secondPlayerEntity.getGameProfile()).thenReturn(new GameProfile(UUID.fromString(secondPlayer.getUuid()), secondPlayer.getUsername()));
    when(secondPlayerEntity.getDisplayName()).thenReturn(new StringTextComponent(secondPlayer.getUsername()));
    PayloadInterface secondPayload = new Payload(secondPlayer, new String[]{"sliprouge", "sliprouge"});
    
    // Scenario
    this.handler.authorizePlayer(this.playerEntity);
    assertTrue(this.handler.isLogged(this.playerEntity));
    CommandSource secondSource = mock(CommandSource.class);
    when(secondSource.asPlayer()).thenReturn(secondPlayerEntity);
    int res = RegisterCommand.execute(secondSource, this.handler, this.guard, secondPayload);
    assertEquals(0, res);
    assertTrue(this.handler.isLogged(secondPlayerEntity));
  }
}
