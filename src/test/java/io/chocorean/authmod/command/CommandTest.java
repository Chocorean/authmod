package io.chocorean.authmod.command;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mojang.authlib.GameProfile;
import io.chocorean.authmod.core.*;
import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.event.Handler;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import jdk.nashorn.internal.ir.annotations.Ignore;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.junit.jupiter.api.Test;

@Ignore
abstract class CommandTest {

  protected Handler handler;
  protected DataSourceStrategyInterface dataSource;
  protected GuardInterface guard;
  protected EntityPlayer playerEntity;
  protected MinecraftServer server;
  protected PayloadInterface payload;
  protected String password;
  protected PlayerInterface player;
  protected ICommand command;
  protected String name;

  public void initProperties(String name) throws IOException {
    File file = Files.createTempFile(CommandTest.class.getSimpleName(), "authmod.csv").toFile();
    Files.deleteIfExists(file.toPath());
    this.handler = new Handler();
    this.player = new Player("Batman", "7128022b-9195-490d-9bc8-9b42ebe2a8e3");
    this.playerEntity = mock(EntityPlayer.class);

    this.server = mock(MinecraftServer.class);
    this.password = "rootrootme";
    this.payload = new Payload(this.player, new String[] { this.password });
    this.dataSource = new FileDataSourceStrategy(file);
    this.guard = new DataSourceGuard(this.dataSource);
    this.name = name;
    when(this.playerEntity.getUniqueID()).thenReturn(new UUID(11, 22));
    when(this.playerEntity.getGameProfile()).thenReturn(new GameProfile(UUID.fromString(player.getUuid()), player.getUsername()));
    when(this.playerEntity.getDisplayName()).thenReturn(new TextComponentString(player.getUsername()));
  }

  @Test
  void testConstructor() {
    assertNotNull(this.command);
  }

  @Test
  void testGetName() {
    assertNotNull(this.command.getName());
  }

  @Test
  void testCheckPermissions() {
    assertTrue(this.command.checkPermission(this.server, this.playerEntity));
  }

  @Test
  void testIsUsernameIndex() {
    assertFalse(this.command.isUsernameIndex(new String[] {}, 0));
  }

  @Test
  void testGetAliases() {
    assertNotNull(this.command.getAliases());
  }

  @Test
  void testExecute() throws CommandException {
    this.command.execute(this.server, this.playerEntity, new String[] {});
  }
}
