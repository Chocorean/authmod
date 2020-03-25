package io.chocorean.authmod.core;

import net.minecraft.entity.player.PlayerEntity;

public class PlayerDescriptor {
  private final PlayerEntity player;
  private final PlayerPos pos;

  public PlayerDescriptor(PlayerEntity entity, PlayerPos position) {
    this.player = entity;
    this.pos = position;
  }

  public PlayerEntity getPlayer() {
    return this.player;
  }

  public PlayerPos getPosition() {
    return this.pos;
  }
}
