package io.chocorean.authmod.core;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerDescriptor {
  private final EntityPlayer player;
  private final PlayerPos pos;

  public PlayerDescriptor(EntityPlayer entity, PlayerPos position) {
    this.player = entity;
    this.pos = position;
  }

  public EntityPlayer getPlayer() {
    return this.player;
  }

  public PlayerPos getPosition() {
    return this.pos;
  }
}
