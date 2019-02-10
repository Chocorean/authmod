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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginCommandTest {
  private LoginCommand loginCommand;
  private Handler handler;
  private IDataSourceStrategy dataSourceStrategy;
  private File dataFile;
  private IPlayer player;
  private EntityPlayerMP sender;
  private Registrator registrator;

  @BeforeEach
  void init() {
    this.dataFile = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
    if (dataFile.exists()) dataFile.delete();
    this.handler = new Handler();
    this.player = PlayerFactory.create();
    this.sender = mock(EntityPlayerMP.class);

    when(this.sender.getGameProfile())
        .thenReturn(new GameProfile(UUID.fromString(player.getUuid()), player.getUsername()));
    when(this.sender.getDisplayNameString()).thenReturn(player.getUsername());
    NetHandlerPlayServer conn = mock(NetHandlerPlayServer.class);
    ((EntityPlayerMP) sender).connection = conn;
    this.dataSourceStrategy = new FileDataSourceStrategy(this.dataFile);
    this.registrator = new Registrator(this.dataSourceStrategy);
    this.loginCommand = new LoginCommand(this.handler, this.dataSourceStrategy);
  }

  @Test
  public void testConstructor() {
    assertNotNull(this.loginCommand);
  }

  @Test
  public void testExecute() throws AuthmodException {
    this.registrator.register(
        io.chocorean.authmod.guard.PlayerFactory.createRegistrationFactoryFromPlayer(this.player));
    this.loginCommand.execute(null, sender, new String[] {player.getPassword()});
    assertTrue(this.handler.isLogged(this.sender));
  }

  @Test
  public void testExecuteWrongPassword() throws AuthmodException {
    this.registrator.register(
        io.chocorean.authmod.guard.PlayerFactory.createRegistrationFactoryFromPlayer(this.player));
    this.loginCommand.execute(null, sender, new String[] {"wrongpass"});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  public void testExecuteWrongNumberOfArgs() {
    this.loginCommand.execute(
        null, sender, new String[] {"test", "test2", player.getEmail(), player.getPassword()});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  public void testExecuteEmailRequiredCorrect() throws AuthmodException {
    this.registrator = new Registrator(this.dataSourceStrategy);
    this.registrator.register(
        io.chocorean.authmod.guard.PlayerFactory.createRegistrationFactoryFromPlayer(this.player));
    this.loginCommand = new LoginCommand(this.handler, this.dataSourceStrategy, true);
    this.loginCommand.execute(null, sender, new String[] {player.getEmail(), player.getPassword()});
    assertTrue(this.handler.isLogged(this.sender));
  }

  @Test
  public void testExecuteWrongUsername() throws AuthmodException {
    when(this.sender.getDisplayNameString()).thenReturn("hacker");
    this.registrator.register(
        io.chocorean.authmod.guard.PlayerFactory.createRegistrationFactoryFromPlayer(this.player));
    this.loginCommand = new LoginCommand(this.handler, this.dataSourceStrategy, false);
    this.loginCommand.execute(null, sender, new String[] {player.getPassword()});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  public void testExecuteEmailRequiredIncorrect() throws AuthmodException {
    this.registrator = new Registrator(this.dataSourceStrategy);
    this.registrator.register(
        io.chocorean.authmod.guard.PlayerFactory.createRegistrationFactoryFromPlayer(this.player));
    this.loginCommand = new LoginCommand(this.handler, this.dataSourceStrategy, true);
    this.loginCommand.execute(null, sender, new String[] {"wrongpass"});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  public void testExecuteAlreadyLogged() throws AuthmodException {
    this.registrator.register(
        io.chocorean.authmod.guard.PlayerFactory.createRegistrationFactoryFromPlayer(this.player));
    handler.authorizePlayer(sender);
    assertTrue(this.handler.isLogged(this.sender));
    this.loginCommand.execute(null, sender, new String[] {"wrongpass"});
    assertTrue(this.handler.isLogged(this.sender));
  }

  @Test
  public void testGetName() {
    assertNotNull(this.loginCommand.getName());
  }

  @Test
  public void testGetUsage() {
    assertNotNull(this.loginCommand.getUsage(null));
  }

  @Test
  public void testGetAliases() {
    assertNotNull(this.loginCommand.getAliases());
  }

  @Test
  public void testCheckPermissions() {
    assertTrue(this.loginCommand.checkPermission(null, null));
  }

  @Test
  public void testGetTabCompletions() {
    assertNotNull(this.loginCommand.getTabCompletions(null, null, null, null));
  }

  @Test
  public void testisUsernameIndex() {
    assertTrue(this.loginCommand.isUsernameIndex(null, 0));
  }

  @Test
  public void testCompareTo() {
    assertEquals(0, this.loginCommand.compareTo(new LoginCommand(null, null)));
  }
}
