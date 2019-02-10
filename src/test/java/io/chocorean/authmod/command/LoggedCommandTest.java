package io.chocorean.authmod.command;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import io.chocorean.authmod.event.Handler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoggedCommandTest {
  private LoggedCommand loggedCommand;
  private Handler handler;

  @BeforeEach
  void init() {
    this.handler = new Handler();
    this.loggedCommand = new LoggedCommand(handler);
  }

  @Test
  public void testConstructor() {
    assertNotNull(this.loggedCommand);
  }

  @Test
  public void testExecuteNotLogged() {
    EntityPlayer sender = mock(EntityPlayerMP.class);
    NetHandlerPlayServer conn = mock(NetHandlerPlayServer.class);
    ((EntityPlayerMP) sender).connection = conn;
    this.loggedCommand.execute(null, sender, null);
    assertFalse(handler.isLogged(sender));
  }

  @Test
  public void testExecute() {
    EntityPlayer sender = mock(EntityPlayerMP.class);
    NetHandlerPlayServer conn = mock(NetHandlerPlayServer.class);
    ((EntityPlayerMP) sender).connection = conn;
    handler.authorizePlayer(sender);
    this.loggedCommand.execute(null, sender, null);
    assertTrue(handler.isLogged(sender));
  }

  @Test
  public void testGetName() {
    assertNotNull(this.loggedCommand.getName());
  }

  @Test
  public void testGetUsage() {
    assertNotNull(this.loggedCommand.getUsage(null));
  }

  @Test
  public void testGetAliases() {
    assertNotNull(this.loggedCommand.getAliases());
  }

  @Test
  public void testCheckPermissions() {
    assertTrue(this.loggedCommand.checkPermission(null, null));
  }

  @Test
  public void testGetTabCompletions() {
    assertNotNull(this.loggedCommand.getTabCompletions(null, null, null, null));
  }

  @Test
  public void testisUsernameIndex() {
    assertTrue(this.loggedCommand.isUsernameIndex(null, 0));
  }

  @Test
  public void testCompareTo() {
    assertEquals(0, this.loggedCommand.compareTo(new LoggedCommand(null)));
  }
}
