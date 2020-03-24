package io.chocorean.authmod.command;

import com.mojang.authlib.GameProfile;
import io.chocorean.authmod.core.Player;
import io.chocorean.authmod.core.PlayerInterface;
import io.chocorean.authmod.event.Handler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoggedCommandTest {
  private LoggedCommand command;
  private Handler handler;
  private EntityPlayerMP sender;

  @BeforeEach
  void init() {
    this.handler = new Handler();
    PlayerInterface player = new Player("Batman", "7128022b-9195-490d-9bc8-9b42ebe2a8e3");
    this.sender = mock(EntityPlayerMP.class);
    when(this.sender.getGameProfile()).thenReturn(new GameProfile(UUID.fromString(player.getUuid()), player.getUsername()));
    when(this.sender.getDisplayNameString()).thenReturn(player.getUsername());
    sender.connection = mock(NetHandlerPlayServer.class);
    this.command = new LoggedCommand(this.handler);
  }

  @Test
  void testConstructor() {
    assertNotNull(this.command);
  }

  @Test
  void testExecute() {
    this.command.execute(mock(MinecraftServer.class), sender, new String[] {});
    assertFalse(this.handler.isLogged(this.sender));
  }

  @Test
  void testExecuteAlreadyLogged() {
    handler.authorizePlayer(sender);
    assertTrue(this.handler.isLogged(this.sender));
    this.command.execute(mock(MinecraftServer.class), sender, new String[] {});
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
    assertEquals(0, this.command.compareTo(new LoggedCommand(null)));
  }
}
