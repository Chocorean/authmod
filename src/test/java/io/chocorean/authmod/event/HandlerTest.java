package io.chocorean.authmod.event;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import net.minecraft.entity.player.EntityPlayer;
import org.junit.jupiter.api.Test;

class HandlerTest {

  @Test
  public void testConstructor() {
    assertNotNull(new Handler());
  }

  @Test
  public void testIsLogged() {
    Handler handler = new Handler();
    EntityPlayer player = mock(EntityPlayer.class);
    assertFalse(handler.isLogged(player));
  }

  @Test
  public void testAuthorizePlayer() {
    Handler handler = new Handler();
    EntityPlayer player = mock(EntityPlayer.class);
    handler.authorizePlayer(player);
    assertTrue(handler.isLogged(player));
  }
}
