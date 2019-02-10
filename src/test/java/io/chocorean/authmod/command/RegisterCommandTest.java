package io.chocorean.authmod.command;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mojang.authlib.GameProfile;
import io.chocorean.authmod.PlayerFactory;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.exception.AuthmodException;
import io.chocorean.authmod.guard.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.guard.datasource.IDataSourceStrategy;
import io.chocorean.authmod.guard.registration.Registrator;
import io.chocorean.authmod.model.IPlayer;
import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterCommandTest {
  private RegisterCommand registerCommand;
  private Handler handler;
  private IDataSourceStrategy dataSourceStrategy;
    private IPlayer player;
  private EntityPlayerMP sender;
  private Registrator registrator;

  @BeforeEach
  void init() {
      File dataFile = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
    if (dataFile.exists()) dataFile.delete();
    this.handler = new Handler();
    this.player = PlayerFactory.create();
    this.sender = mock(EntityPlayerMP.class);

    when(this.sender.getGameProfile())
        .thenReturn(new GameProfile(UUID.fromString(player.getUuid()), player.getUsername()));
    when(this.sender.getDisplayNameString()).thenReturn(player.getUsername());
    sender.connection = mock(NetHandlerPlayServer.class);
    this.dataSourceStrategy = new FileDataSourceStrategy(dataFile);
    this.registrator = new Registrator(this.dataSourceStrategy);
    this.registerCommand = new RegisterCommand(this.handler, this.dataSourceStrategy);
  }

  @Test
  void testConstructor() {
    assertNotNull(this.registerCommand);
  }

  @Test
  void testExecuteWrongNumberOfArgs() {
    this.registerCommand.execute(
        mock(MinecraftServer.class), sender, new String[] {"test", "test2", player.getEmail(), player.getPassword()});
    assertNull(this.dataSourceStrategy.find(null, this.player.getUsername()));
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecute() {
    this.registrator = new Registrator(this.dataSourceStrategy);
    this.registerCommand.execute(
      mock(MinecraftServer.class), sender, new String[] {player.getPassword(), player.getPassword()});
    assertNotNull(this.dataSourceStrategy.find(null, this.player.getUsername()));
    assertTrue(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteWithEmailRequired() {
    this.registerCommand = new RegisterCommand(this.handler, this.dataSourceStrategy, true);
    this.registrator = new Registrator(this.dataSourceStrategy);
    this.registerCommand.execute(
      mock(MinecraftServer.class), sender, new String[] {player.getEmail(), player.getPassword(), player.getPassword()});
    assertNotNull(this.dataSourceStrategy.find(this.player.getEmail(), null));
    assertTrue(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteWithEmailRequiredIncorrect() {
    this.registerCommand = new RegisterCommand(this.handler, this.dataSourceStrategy, true);
    this.registrator = new Registrator(this.dataSourceStrategy);
    this.registerCommand.execute(
      mock(MinecraftServer.class), sender, new String[] {player.getPassword(), player.getPassword()});
    assertNull(this.dataSourceStrategy.find(this.player.getEmail(), null));
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteEmailRequiredIncorrect() {
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteAlreadyLogged() {
    handler.authorizePlayer(sender);
    assertTrue(this.handler.isLogged(this.sender));
    this.registerCommand.execute(
      mock(MinecraftServer.class), sender, new String[] {player.getPassword(), player.getPassword()});
    assertTrue(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecutePlayerAlreadyExists() throws AuthmodException {
    this.registrator.register(
        io.chocorean.authmod.guard.PlayerFactory.createRegistrationFactoryFromPlayer(player));
    this.registerCommand.execute(
      mock(MinecraftServer.class), sender, new String[] {player.getPassword(), player.getPassword()});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  void testGetName() {
    assertNotNull(this.registerCommand.getName());
  }

  @Test
  void testGetUsage() {
    assertNotNull(this.registerCommand.getUsage(mock(ICommandSender.class)));
  }

  @Test
  void testGetAliases() {
    assertNotNull(this.registerCommand.getAliases());
  }

  @Test
  void testCheckPermissions() {
    assertTrue(this.registerCommand.checkPermission(mock(MinecraftServer.class), mock(ICommandSender.class)));
  }

  @Test
  void testGetTabCompletions() {
    assertNotNull(this.registerCommand.getTabCompletions(mock(MinecraftServer.class), mock(ICommandSender.class), new String[]{}, mock(BlockPos.class)));
  }

  @Test
  void testisUsernameIndex() {
    assertTrue(this.registerCommand.isUsernameIndex(new String[]{}, 0));
  }

  @Test
  void testCompareTo() {
    assertEquals(
        0, this.registerCommand.compareTo(new RegisterCommand(this.handler, new FileDataSourceStrategy())));
  }
}
