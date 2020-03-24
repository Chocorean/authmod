package io.chocorean.authmod.core;

import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlayerPosTest {

  private PlayerPos position;

  @BeforeEach
  public void init() {
    this.position = new PlayerPos(new BlockPos(1, 2, 3), 1, 2);
  }

  @Test
  public void testGetYaw() {
    assertEquals(1, this.position.getYaw());
  }

  @Test
  public void testGetPitch() {
    assertEquals(2, this.position.getPitch());
  }

  @Test
  public void testGetPosition() {
    assertNotNull( this.position.getPosition());
  }

}
