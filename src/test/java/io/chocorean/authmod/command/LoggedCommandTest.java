package io.chocorean.authmod.command;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import io.chocorean.authmod.event.Handler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoggedCommandTest {
  private LoggedCommand loggedCommand;
  private Handler handler;
  private EntityPlayerMP sender;

  @BeforeEach
  void init() {
    this.handler = new Handler();
    this.loggedCommand = new LoggedCommand(handler);
    this.sender = mock(EntityPlayerMP.class);
    this.sender.connection = mock(NetHandlerPlayServer.class);
  }

  @Test
  void testConstructor() {
    assertNotNull(this.loggedCommand);
  }

  @Test
  void testExecuteNotLogged() {
    this.loggedCommand.execute(mock(MinecraftServer.class), sender, new String[] {});
    assertFalse(handler.isLogged(sender));
  }

  @Test
  void testExecute() {
    handler.authorizePlayer(sender);
    this.loggedCommand.execute(mock(MinecraftServer.class), sender, new String[] {});
    assertTrue(handler.isLogged(sender));
  }

  @Test
  void testGetName() {
    assertNotNull(this.loggedCommand.getName());
  }

  @Test
  void testGetUsage() {
    assertNotNull(this.loggedCommand.getUsage(mock(ICommandSender.class)));
  }

  @Test
  void testGetAliases() {
    assertNotNull(this.loggedCommand.getAliases());
  }

  @Test
  void testCheckPermissions() {
    assertTrue(
        this.loggedCommand.checkPermission(
            mock(MinecraftServer.class), mock(ICommandSender.class)));
  }

  @Test
  void testGetTabCompletions() {
    assertNotNull(
        this.loggedCommand.getTabCompletions(
            mock(MinecraftServer.class),
            mock(ICommandSender.class),
            new String[] {},
            mock(BlockPos.class)));
  }

  @Test
  void testisUsernameIndex() {
    assertTrue(this.loggedCommand.isUsernameIndex(new String[] {}, 0));
  }

  @Test
  void testCompareTo() {
    assertEquals(0, this.loggedCommand.compareTo(new LoggedCommand(this.handler)));
  }
}
