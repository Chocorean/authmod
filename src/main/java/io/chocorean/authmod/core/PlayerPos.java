package io.chocorean.authmod.core;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class PlayerPos {
  private final Vector3d pos;
  private final float yaw;
  private final float pitch;

  public PlayerPos(Vector3d pos, float yaw, float pitch) {
    this.pos = pos;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public Vector3d getPosition() {
    return pos;
  }

  public float getYaw() {
    return yaw;
  }

  public float getPitch() {
    return pitch;
  }
}
