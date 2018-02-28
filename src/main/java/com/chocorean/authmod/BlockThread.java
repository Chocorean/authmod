package com.chocorean.authmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class BlockThread extends Thread {
    private EntityPlayer player;
    private BlockPos pos;

    private static boolean correctPassword=false;

    BlockThread(EntityPlayer entity, BlockPos position){
        player=entity;
        pos = position;
    }
    public void run() {
        player.addChatComponentMessage(new TextComponentString("Use /register <password> <password>"));
        while(!isAuth(player)){
        }
        player.addChatComponentMessage(new TextComponentString("Auth successful."));
        player.setPosition(pos.getX(),pos.getY(),pos.getZ());
    }

    private boolean isAuth(EntityPlayer player){
        return correctPassword;
    }
}
