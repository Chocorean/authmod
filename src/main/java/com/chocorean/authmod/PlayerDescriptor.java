package com.chocorean.authmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class PlayerDescriptor {
    private EntityPlayer player;

    PlayerDescriptor(EntityPlayer entity, BlockPos position){
        player=entity;
        player.addChatComponentMessage(new TextComponentString("Use /register <password> <password> or /login <password> to play."));
    }

    // Getters
    public EntityPlayer getPlayer() {
        return player;
    }
}
