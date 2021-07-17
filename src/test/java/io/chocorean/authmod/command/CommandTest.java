package io.chocorean.authmod.command;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.core.*;
import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.event.Handler;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.UUID;
import jdk.nashorn.internal.ir.annotations.Ignore;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import org.junit.jupiter.api.Test;

@Ignore
abstract class CommandTest {

  protected Handler handler;
  protected DataSourceStrategyInterface dataSource;
  protected GuardInterface guard;
  protected CommandContext<CommandSource> context;
  protected ServerPlayerEntity playerEntity;
  protected CommandSource source;
  protected PayloadInterface payload;
  protected String password;
  protected PlayerInterface player;
  protected CommandInterface command;
  protected String name;

  public void initProperties(String name) throws Exception {
    File file = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.db").toFile();
    Files.deleteIfExists(file.toPath());
    this.handler = new Handler();
    this.player = new Player("Batman", "7128022b-9195-490d-9bc8-9b42ebe2a8e3");
    this.playerEntity = mock(ServerPlayerEntity.class);
    this.password = "rootrootme";
    this.payload = new Payload(this.player, new String[] { this.password });
    this.context = mock(CommandContext.class);
    this.source = mock(CommandSource.class);
    this.dataSource = new FileDataSourceStrategy(file);
    this.guard = new DataSourceGuard(this.dataSource);
    this.name = name;
    when(this.source.getPlayerOrException()).thenReturn(this.playerEntity);
    when(this.playerEntity.getGameProfile()).thenReturn(new GameProfile(UUID.fromString(player.getUuid()), player.getUsername()));
    when(this.playerEntity.getDisplayName()).thenReturn(new StringTextComponent(player.getUsername()));
    when(this.context.getSource()).thenReturn(this.source);
  }

  @Test
  void testConstructor() {
    assertNotNull(this.command);
  }

  @Test
  void testGetParameters() {
    assertNotNull(this.command.getParameters());
  }

  @Test
  void testGetCommandBuilder() {
    assertEquals(this.name, this.command.getCommandBuilder().getLiteral());
  }

  @Test
  void testRun() throws CommandSyntaxException {
    assertNotEquals(0, this.command.run(this.context));
  }
}
