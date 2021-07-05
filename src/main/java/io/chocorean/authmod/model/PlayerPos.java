package io.chocorean.authmod.model;

import net.minecraft.util.math.Vec3d;

public class PlayerPos {
  private final Vec3d pos;
  private final float yaw, pitch;

  public PlayerPos(Vec3d pos, float yaw, float pitch) {
    this.pos = pos;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public Vec3d getPosition() {
    return pos;
  }

  public float getYaw() {
    return yaw;
  }

  public float getPitch() {
    return pitch;
  }
}
