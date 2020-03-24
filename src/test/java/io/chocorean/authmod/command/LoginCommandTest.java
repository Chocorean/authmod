package io.chocorean.authmod.command;

import com.mojang.authlib.GameProfile;
import io.chocorean.authmod.core.*;
import io.chocorean.authmod.core.datasource.DataSourcePlayer;
import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.event.Handler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginCommandTest {
  private LoginCommand command;
  private Handler handler;
  private DataSourceStrategyInterface dataSource;
  private PlayerInterface player;
  private GuardInterface guard;
  private EntityPlayerMP sender;
  private String password;

  @BeforeEach
  void init() throws Exception {
    File file = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
    Files.deleteIfExists(file.toPath());
    this.handler = new Handler();
    this.player = new Player("Batman", "7128022b-9195-490d-9bc8-9b42ebe2a8e3");
    this.sender = mock(EntityPlayerMP.class);

    when(this.sender.getGameProfile()).thenReturn(new GameProfile(UUID.fromString(player.getUuid()), player.getUsername()));
    when(this.sender.getDisplayNameString()).thenReturn(player.getUsername());
    sender.connection = mock(NetHandlerPlayServer.class);
    this.password = "rootrootme";
    this.dataSource = new FileDataSourceStrategy(file);
    this.guard = new DataSourceGuard(this.dataSource);
    PayloadInterface payload = new Payload(this.player, new String[]{password, password});
    this.command = new LoginCommand(this.handler, this.guard);
    this.guard.register(payload);
  }

  @Test
  void testConstructor() {
    assertNotNull(this.command);
  }

  @Test
  void testExecute() {
    this.command.execute(mock(MinecraftServer.class), sender, new String[] {this.password});
    assertTrue(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteWrongPassword() {
    this.command.execute(mock(MinecraftServer.class), sender, new String[] {"wrongpass"});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteWrongNumberOfArgs() {
    this.command.execute(mock(MinecraftServer.class), sender, new String[] {"test", "test2", player.getUsername()});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteIdentifierRequired() {
    this.guard = new DataSourceGuard(this.dataSource, true);
    this.command = new LoginCommand(this.handler, this.guard);
    this.command.execute(mock(MinecraftServer.class), sender, new String[] {"mcdostone", password});
    assertTrue(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteBanned() {
    FileDataSourceStrategy mock  = mock(FileDataSourceStrategy.class);
    when(mock.find( player.getUsername())).thenReturn(new DataSourcePlayer(player).setBanned(true));
    this.command = new LoginCommand(this.handler, new DataSourceGuard(mock));
    this.command.execute(mock(MinecraftServer.class), sender, new String[] {password});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecutePlayerNotFound() {
    when(this.sender.getDisplayNameString()).thenReturn("not_exist");
    this.command.execute(mock(MinecraftServer.class), sender, new String[] {"ninja-turtles"});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteEmailRequiredMissing() {
    this.command = new LoginCommand(this.handler, new DataSourceGuard(this.dataSource, true));
    this.command.execute(mock(MinecraftServer.class), sender, new String[] {"wrongpass"});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteAlreadyLogged() {
    handler.authorizePlayer(sender);
    assertTrue(this.handler.isLogged(this.sender));
    this.command.execute(mock(MinecraftServer.class), sender, new String[] {"wrongpass"});
    assertTrue(this.handler.isLogged(this.sender));
  }

  @Test
  void testGetName() {
    assertNotNull(this.command.getName());
  }

  @Test
  void testGetUsage() {
    assertNotNull(this.command.getUsage(mock(ICommandSender.class)));
  }

  @Test
  void testGetAliases() {
    assertNotNull(this.command.getAliases());
  }

  @Test
  void testCheckPermissions() {
    assertTrue(
        this.command.checkPermission(mock(MinecraftServer.class), mock(ICommandSender.class)));
  }

  @Test
  void testGetTabCompletions() {
    assertNotNull(this.command.getTabCompletions(mock(MinecraftServer.class), mock(ICommandSender.class), new String[] {}, mock(BlockPos.class)));
  }

  @Test
  void testisUsernameIndex() {
    assertTrue(this.command.isUsernameIndex(new String[] {}, 0));
  }

  @Test
  void testCompareTo() {
    assertEquals(0, this.command.compareTo(new LoginCommand(null, null)));
  }
}
