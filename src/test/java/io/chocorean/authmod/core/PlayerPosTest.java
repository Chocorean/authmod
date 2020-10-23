package io.chocorean.authmod.core;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlayerPosTest {

  private PlayerPos position;

  @BeforeEach
  void init() {
    this.position = new PlayerPos(new Vector3d(1, 2, 3), 1, 2);
  }

  @Test
  void testGetYaw() {
    assertEquals(1, this.position.getYaw());
  }

  @Test
  void testGetPitch() {
    assertEquals(2, this.position.getPitch());
  }

  @Test
  void testGetPosition() {
    assertNotNull( this.position.getPosition());
  }

}
