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

class LoginCommandTest {
  private LoginCommand loginCommand;
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
    this.loginCommand = new LoginCommand(this.handler, this.dataSourceStrategy);
  }

  @Test
  void testConstructor() {
    assertNotNull(this.loginCommand);
  }

  @Test
  void testExecute() throws AuthmodException {
    this.registrator.register(
        io.chocorean.authmod.guard.PlayerFactory.createRegistrationFactoryFromPlayer(this.player));
    this.loginCommand.execute(
        mock(MinecraftServer.class), sender, new String[] {player.getPassword()});
    assertTrue(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteWrongPassword() throws AuthmodException {
    this.registrator.register(
        io.chocorean.authmod.guard.PlayerFactory.createRegistrationFactoryFromPlayer(this.player));
    this.loginCommand.execute(mock(MinecraftServer.class), sender, new String[] {"wrongpass"});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteWrongNumberOfArgs() {
    this.loginCommand.execute(
        mock(MinecraftServer.class),
        sender,
        new String[] {"test", "test2", player.getEmail(), player.getPassword()});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteEmailRequiredCorrect() throws AuthmodException {
    this.registrator = new Registrator(this.dataSourceStrategy);
    this.registrator.register(
        io.chocorean.authmod.guard.PlayerFactory.createRegistrationFactoryFromPlayer(this.player));
    this.loginCommand = new LoginCommand(this.handler, this.dataSourceStrategy, true);
    this.loginCommand.execute(
        mock(MinecraftServer.class),
        sender,
        new String[] {player.getEmail(), player.getPassword()});
    assertTrue(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteWrongUsername() throws AuthmodException {
    when(this.sender.getDisplayNameString()).thenReturn("hacker");
    this.registrator.register(
        io.chocorean.authmod.guard.PlayerFactory.createRegistrationFactoryFromPlayer(this.player));
    this.loginCommand = new LoginCommand(this.handler, this.dataSourceStrategy, false);
    this.loginCommand.execute(
        mock(MinecraftServer.class), sender, new String[] {player.getPassword()});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteEmailRequiredIncorrect() throws AuthmodException {
    this.registrator = new Registrator(this.dataSourceStrategy);
    this.registrator.register(
        io.chocorean.authmod.guard.PlayerFactory.createRegistrationFactoryFromPlayer(this.player));
    this.loginCommand = new LoginCommand(this.handler, this.dataSourceStrategy, true);
    this.loginCommand.execute(mock(MinecraftServer.class), sender, new String[] {"wrongpass"});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteAlreadyLogged() throws AuthmodException {
    this.registrator.register(
        io.chocorean.authmod.guard.PlayerFactory.createRegistrationFactoryFromPlayer(this.player));
    handler.authorizePlayer(sender);
    assertTrue(this.handler.isLogged(this.sender));
    this.loginCommand.execute(mock(MinecraftServer.class), sender, new String[] {"wrongpass"});
    assertTrue(this.handler.isLogged(this.sender));
  }

  @Test
  void testGetName() {
    assertNotNull(this.loginCommand.getName());
  }

  @Test
  void testGetUsage() {
    assertNotNull(this.loginCommand.getUsage(mock(ICommandSender.class)));
  }

  @Test
  void testGetAliases() {
    assertNotNull(this.loginCommand.getAliases());
  }

  @Test
  void testCheckPermissions() {
    assertTrue(
        this.loginCommand.checkPermission(mock(MinecraftServer.class), mock(ICommandSender.class)));
  }

  @Test
  void testGetTabCompletions() {
    assertNotNull(
        this.loginCommand.getTabCompletions(
            mock(MinecraftServer.class),
            mock(ICommandSender.class),
            new String[] {},
            mock(BlockPos.class)));
  }

  @Test
  void testisUsernameIndex() {
    assertTrue(this.loginCommand.isUsernameIndex(new String[] {}, 0));
  }

  @Test
  void testCompareTo() {
    assertEquals(0, this.loginCommand.compareTo(new LoginCommand(null, null)));
  }
}
