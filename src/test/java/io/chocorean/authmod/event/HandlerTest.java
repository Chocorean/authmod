package io.chocorean.authmod.event;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import net.minecraft.entity.player.EntityPlayer;
import org.junit.jupiter.api.Test;

class HandlerTest {

  @Test
  void testConstructor() {
    assertNotNull(new Handler());
  }

  @Test
  void testIsLogged() {
    Handler handler = new Handler();
    EntityPlayer player = mock(EntityPlayer.class);
    assertFalse(handler.isLogged(player));
  }

  @Test
  void testAuthorizePlayer() {
    Handler handler = new Handler();
    EntityPlayer player = mock(EntityPlayer.class);
    handler.authorizePlayer(player);
    assertTrue(handler.isLogged(player));
  }
}
