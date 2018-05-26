package com.chocorean.authmod.events;

import com.chocorean.authmod.AuthMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber
public class Handler {
    private static ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

    @SubscribeEvent(priority= EventPriority.HIGHEST)
    public static void onJoin(PlayerLoggedInEvent event){
        EntityPlayer entity = event.player;
        // initializing timer for kicking player if he/she hasn't logged in a minute
        BlockPos pos = entity.getPosition();
        PlayerDescriptor dc = new PlayerDescriptor(entity, pos);
        AuthMod.descriptors.put(entity, dc);
        scheduler.schedule(() -> {
            if(AuthMod.descriptors.containsKey(entity)) {
                AuthMod.descriptors.remove(entity);
                entity.setPositionAndUpdate(
                        dc.getPosition().getX(),
                        dc.getPosition().getY(),
                        dc.getPosition().getZ()
                );
                ((EntityPlayerMP) entity).connection.kickPlayerFromServer("You took too many time to log in.");
            }
        }, 10, TimeUnit.SECONDS);
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onPlayerEvent(PlayerEvent event) {
        EntityPlayer entity = event.getEntityPlayer();
        if(AuthMod.descriptors.containsKey(entity)) {
            if (event.isCancelable()) {
                event.setCanceled(true);
                ((EntityPlayerMP) entity).connection.sendPacket(new SPacketChat(new TextComponentString("You have to use /register ou /login to play.")));
            }
        }
    }

    @SubscribeEvent(priority= EventPriority.HIGHEST)
    public static void onCommand(CommandEvent event){
        String name = event.getCommand().getCommandName();
        if (!(name.equals("register") || name.equals("login")) && (event.getSender() instanceof EntityPlayer)) {
            if (event.isCancelable()){
                event.setCanceled(true);
                ((EntityPlayerMP)event.getSender()).connection.sendPacket(new SPacketChat(new TextComponentString("You have to use /register ou /login to use commands.")));
            }
        }
    }
}
