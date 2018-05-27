package com.chocorean.authmod.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class PlayerDescriptor {

    private final EntityPlayer player;
    private final BlockPos pos;

    PlayerDescriptor(EntityPlayer entity, BlockPos position){
        player = entity;
        pos = position;
        player.addChatComponentMessage(new TextComponentString("Use /login <email> <password> to play."));
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public BlockPos getPosition() {
        return pos;
    }
}
