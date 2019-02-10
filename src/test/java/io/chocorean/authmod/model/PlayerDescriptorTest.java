package io.chocorean.authmod.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import net.minecraft.entity.player.EntityPlayerMP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerDescriptorTest {
  private PlayerDescriptor playerDescriptor;

  @BeforeEach
  void init() {
    this.playerDescriptor =
        new PlayerDescriptor(mock(EntityPlayerMP.class), new PlayerPos(null, 1, 0));
  }

  @Test
  void testConstructor() {
    assertNotNull(this.playerDescriptor);
  }

  @Test
  void testGetPlayer() {
    assertNotNull(this.playerDescriptor.getPlayer());
  }

  @Test
  void testGetPosition() {
    assertNotNull(this.playerDescriptor.getPosition());
  }
}
