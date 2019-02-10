package io.chocorean.authmod.model;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerDescriptor {
    private final EntityPlayer player;
    private final PlayerPos pos;

    public PlayerDescriptor(EntityPlayer entity, PlayerPos position) {
        player = entity;
        pos = position;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public PlayerPos getPosition() {
        return pos;
    }

}

