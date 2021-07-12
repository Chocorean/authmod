package io.chocorean.authmod.event;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import net.minecraft.entity.player.PlayerEntity;
import org.junit.jupiter.api.Test;

class HandlerTest {

  @Test
  void testConstructor() {
    assertNotNull(new Handler());
  }

  @Test
  void testIsLogged() {
    Handler handler = new Handler();
    PlayerEntity player = mock(PlayerEntity.class);
    assertFalse(handler.isLogged(player));
  }

  @Test
  void testAuthorizePlayer() {
    Handler handler = new Handler();
    PlayerEntity player = mock(PlayerEntity.class);
    handler.authorizePlayer(player);
    assertTrue(handler.isLogged(player));
  }
}
