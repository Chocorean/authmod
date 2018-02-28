package com.chocorean.authmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedList;

@Mod.EventBusSubscriber
public class Handler {
    public static LinkedList<PlayerDescriptor> desc = new LinkedList();

    @SubscribeEvent(priority= EventPriority.HIGHEST)
    public static void onJoin(EntityJoinWorldEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayer && !entity.isDead) {
            ((EntityPlayer) entity).addChatComponentMessage(new TextComponentString("hello"));
            World world = entity.getEntityWorld();
            BlockPos pos = entity.getPosition();
            if (!world.isRemote){
                PlayerDescriptor dc = new PlayerDescriptor((EntityPlayer)entity, pos);
                desc.add(dc);
            }
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onPlayerEvent(PlayerEvent event) {
        for (PlayerDescriptor dc : Handler.desc) {
            Entity entity = event.getEntity();
            World world = entity.getEntityWorld();
            if (dc.getPlayer().getName().equals(entity.getName())) {
                // player still havent logged in successfully
                if (event.isCancelable()) {
                    event.setCanceled(true);
                    entity.addChatMessage(new TextComponentString("You have to use /register ou /login to play."));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCommand(CommandEvent event){
        if (!(event.getSender() instanceof EntityPlayer))
            return;
        String name = event.getCommand().getCommandName();
        if (!(name.equals("register") || name.equals("login"))) {
            if (event.isCancelable()){
                event.setCanceled(true);
                event.getSender().addChatMessage(new TextComponentString("You have to use /register ou /login to use commands."));
            }
        }
    }
}
