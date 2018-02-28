package com.chocorean.authmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedList;

public class Handler {
    public static LinkedList<Thread> threads = new LinkedList<>();

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent event){
        Entity entity = event.getEntity();
        World world = event.getWorld();
        BlockPos pos = entity.getPosition();
        if (entity instanceof EntityPlayer && !entity.isDead) {
            if (!world.isRemote){
                threads.add(new Thread(new BlockThread((EntityPlayer)entity, pos)));
                threads.getLast().start();
            }
        }
    }
}
