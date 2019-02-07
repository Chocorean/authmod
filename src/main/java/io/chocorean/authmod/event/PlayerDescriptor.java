package io.chocorean.authmod.event;

import io.chocorean.authmod.model.PlayerPos;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerDescriptor {

    private final EntityPlayer player;
    private final PlayerPos pos;

    PlayerDescriptor(EntityPlayer entity, PlayerPos position){
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
