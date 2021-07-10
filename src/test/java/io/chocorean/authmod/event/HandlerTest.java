package io.chocorean.authmod.event;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import net.minecraft.entity.player.PlayerEntity;
import org.junit.jupiter.api.Test;

class HandlerTest {

  @Test
  public void testConstructor() {
    assertNotNull(new Handler());
  }

  @Test
  public void testIsLogged() {
    Handler handler = new Handler();
    PlayerEntity player = mock(PlayerEntity.class);
    assertFalse(handler.isLogged(player));
  }

  @Test
  public void testAuthorizePlayer() {
    Handler handler = new Handler();
    PlayerEntity player = mock(PlayerEntity.class);
    handler.authorizePlayer(player);
    assertTrue(handler.isLogged(player));
  }
}
