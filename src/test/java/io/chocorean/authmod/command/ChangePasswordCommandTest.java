package io.chocorean.authmod.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import io.chocorean.authmod.core.DataSourceGuard;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.Payload;
import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.core.Player;
import io.chocorean.authmod.core.PlayerInterface;
import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.event.Handler;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class ChangePasswordCommandTest {
  private Handler handler;
  private DataSourceStrategyInterface dataSource;
  private GuardInterface guard;
  private ServerPlayerEntity playerEntity;
  private CommandSource source;
  private PayloadInterface payload;
  private String password;
  private PlayerInterface player;
  
  @BeforeEach
  void init() throws IOException, CommandSyntaxException, AuthmodError {
    File file = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
    Files.deleteIfExists(file.toPath());
    this.handler = new Handler();
    this.player = new Player("Batman", "7128022b-9195-490d-9bc8-9b42ebe2a8e3");
    this.playerEntity = mock(ServerPlayerEntity.class);
    this.password = "rootrootme";
    this.payload = new Payload(this.player, new String[]{this.password});
    when(this.playerEntity.getGameProfile()).thenReturn(new GameProfile(UUID.fromString(player.getUuid()), player.getUsername()));
    when(this.playerEntity.getDisplayName()).thenReturn(new StringTextComponent(player.getUsername()));
    this.source = mock(CommandSource.class);
    when(this.source.asPlayer()).thenReturn(this.playerEntity);
    this.dataSource = new FileDataSourceStrategy(file);
    this.guard = new DataSourceGuard(this.dataSource);
    this.guard.register(new Payload(this.player, new String[]{this.password, this.password}));
    this.handler.authorizePlayer(this.playerEntity);
  }
  
  @Test
  void testExecute() {
    PayloadInterface newPayload = new Payload(this.player, new String[] {"anotherone", "anotherone"});
    int res = ChangePasswordCommand.execute(this.source, this.handler, this.guard, this.payload, newPayload);
    assertEquals(1, res);
  }
  
  @Test
  void testWrongOldPassword() {
    PayloadInterface wrongPayload = new Payload(this.player, new String[] {"wrongpass"});
    PayloadInterface newPayload = new Payload(this.player, new String[] {"anotherone"});
    int res = ChangePasswordCommand.execute(this.source, this.handler, this.guard, wrongPayload, newPayload);
    assertEquals(0, res);
  }
  
  @Test
  void testSamePassword() {
    int res = ChangePasswordCommand.execute(this.source, this.handler, this.guard, this.payload, this.payload);
    assertEquals(0, res);
  }
  
  @Test
  void testNotLogged() throws CommandSyntaxException {
    PlayerInterface unloggedPlayer = new Player("Superman", "094b0779-992c-4fac-90f5-fa839dc77dbc");
    ServerPlayerEntity unloggedPlayerEntity = mock(ServerPlayerEntity.class);
    String unloggedOldPassword = "imnotloggedyet";
    PayloadInterface unloggedOldPayload = new Payload(unloggedPlayer, new String[]{unloggedOldPassword});
    when(unloggedPlayerEntity.getGameProfile()).thenReturn(new GameProfile(UUID.fromString(unloggedPlayer.getUuid()), unloggedPlayer.getUsername()));
    when(unloggedPlayerEntity.getDisplayName()).thenReturn(new StringTextComponent(unloggedPlayer.getUsername()));
    PayloadInterface unloggedNewPayload = new Payload(unloggedPlayer, new String[]{"newpassword"});
    when(unloggedPlayerEntity.getGameProfile()).thenReturn(new GameProfile(UUID.fromString(unloggedPlayer.getUuid()), unloggedPlayer.getUsername()));
    when(unloggedPlayerEntity.getDisplayName()).thenReturn(new StringTextComponent(unloggedPlayer.getUsername()));
    CommandSource unloggedSource = mock(CommandSource.class);
    when(unloggedSource.asPlayer()).thenReturn(unloggedPlayerEntity);
    int res = ChangePasswordCommand.execute(unloggedSource, this.handler, this.guard, unloggedOldPayload, unloggedNewPayload);
    assertEquals(0, res);
    assertFalse(this.handler.isLogged(unloggedPlayerEntity));
  }
}
