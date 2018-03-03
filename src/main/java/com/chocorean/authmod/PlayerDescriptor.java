package com.chocorean.authmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class PlayerDescriptor {
    private EntityPlayer player;
    private BlockPos pos;

    PlayerDescriptor(EntityPlayer entity, BlockPos position){
        player=entity;
        pos=position;
        ((EntityPlayerMP)player).connection.sendPacket(new SPacketChat(new TextComponentString("Use /register <password> <password> or /login <password> to play.")));
    }

    // Getters
    public EntityPlayer getPlayer() {
        return player;
    }

    public BlockPos getPos() {
        return pos;
    }
}
