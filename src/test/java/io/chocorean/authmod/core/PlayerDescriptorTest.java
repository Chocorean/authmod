package io.chocorean.authmod.core;

import net.minecraft.entity.player.ServerPlayerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class PlayerDescriptorTest {

  private ServerPlayerEntity player;
  private PlayerDescriptor descriptor;
  private PlayerPos position;

  @BeforeEach
  void init() {
    this.position = new PlayerPos( null, 4, 4);
    this.player = mock(ServerPlayerEntity.class);
    this.descriptor = new PlayerDescriptor(this.player, this.position);
  }
  
  @Test
  void testGetPlayer() {
    assertEquals(descriptor.getPlayer(), player);
  }

  @Test
  void testGetPosition() {
    assertEquals(descriptor.getPosition(), position);
  }

}
