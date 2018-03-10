package com.chocorean.authmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber
public class Handler {
    public static LinkedList<PlayerDescriptor> desc = new LinkedList();

    @SubscribeEvent(priority= EventPriority.HIGHEST)
    public static void onJoin(EntityJoinWorldEvent event){
        final Entity entity = event.getEntity();
        if (entity instanceof EntityPlayer && !entity.isDead) {
            // initializing timer for kicking player if he/she hasn't logged in a minute
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    for (PlayerDescriptor dc : desc){
                        if (dc.getPlayer().getName().equals(entity.getName())) {
                            desc.remove(dc);
                            // back to connection position and kicked
                            ((EntityPlayerMP)entity).setPositionAndUpdate(dc.getPos().getX(),dc.getPos().getY(),dc.getPos().getZ());
                            ((EntityPlayerMP)entity).connection.kickPlayerFromServer("You took too many time to log in.");
                        }
                    }
                }
            },60000);
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

    @SubscribeEvent(priority= EventPriority.HIGHEST)
    public static void onCommand(CommandEvent event){
        if (!(event.getSender() instanceof EntityPlayer))
            return;
        String name = event.getCommand().getCommandName();
        if (!(name.equals("register") || name.equals("login"))) {
            for (PlayerDescriptor dc : Handler.desc) {
                Entity entity = (Entity)event.getSender();
                World world = entity.getEntityWorld();
                if (dc.getPlayer().getName().equals(entity.getName())) {
                    if (event.isCancelable()){
                        event.setCanceled(true);
                        event.getSender().addChatMessage(new TextComponentString("You have to use /register ou /login to use commands."));
                    }
                }
            }
        }
    }
}
