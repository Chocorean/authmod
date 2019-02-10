package io.chocorean.authmod.model;

import net.minecraft.util.math.BlockPos;

public class PlayerPos {
  private final BlockPos pos;
  private final float yaw, pitch;

  public PlayerPos(BlockPos pos, float yaw, float pitch) {
    this.pos = pos;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public BlockPos getPosition() {
    return pos;
  }

  public float getYaw() {
    return yaw;
  }

  public float getPitch() {
    return pitch;
  }
}
