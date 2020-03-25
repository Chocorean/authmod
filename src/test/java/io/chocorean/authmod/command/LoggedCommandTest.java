package io.chocorean.authmod.command;

import com.mojang.authlib.GameProfile;
import io.chocorean.authmod.core.Player;
import io.chocorean.authmod.core.PlayerInterface;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.util.text.ServerLanguageMap;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoggedCommandTest {
  private Handler handler;
  private ServerPlayerEntity playerEntity;
  private CommandSource source;

  @BeforeAll
  static void setUp() {
    ServerLanguageMap.init();
  }

  @BeforeEach
  void init() throws Exception {
    this.handler = new Handler();
    PlayerInterface player = new Player("Batman", "7128022b-9195-490d-9bc8-9b42ebe2a8e3");
    this.playerEntity = mock(ServerPlayerEntity.class);
    when(this.playerEntity.getGameProfile()).thenReturn(new GameProfile(UUID.fromString(player.getUuid()), player.getUsername()));
    when(this.playerEntity.getDisplayName()).thenReturn(new StringTextComponent(player.getUsername()));
    this.source = mock(CommandSource.class);
    when(this.source.asPlayer()).thenReturn(this.playerEntity);
  }

  @Test
  void testExecute() {
    int res = LoggedCommand.execute(this.source, this.handler);
    assertEquals(1, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteAlreadyLogged() {
    handler.authorizePlayer(this.playerEntity);
    assertTrue(this.handler.isLogged(this.playerEntity));
    int res = LoggedCommand.execute(this.source, this.handler);
    assertEquals(1, res);
    assertTrue(this.handler.isLogged(this.playerEntity));
  }

}
