package io.chocorean.authmod.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class PlayerDescriptor {

    private final EntityPlayer player;
    private final BlockPos pos;

    PlayerDescriptor(EntityPlayer entity, BlockPos position){
        player = entity;
        pos = position;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public BlockPos getPosition() {
        return pos;
    }
}
