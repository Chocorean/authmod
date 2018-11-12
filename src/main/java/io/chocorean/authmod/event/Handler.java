package io.chocorean.authmod.event;

import io.chocorean.authmod.AuthMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber
public class Handler {

    private static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
    private static final Map<EntityPlayer, PlayerDescriptor> descriptors = new HashMap<>();
    private static final Map<EntityPlayer, Boolean> logged = new HashMap<>();

    @SubscribeEvent(priority= EventPriority.HIGHEST)
    public static void onJoin(PlayerLoggedInEvent event){
        EntityPlayer entity = event.player;
        ((EntityPlayerMP)entity).connection.sendPacket(new SPacketChat(new TextComponentString(AuthMod.getConfig().getWelcomeMessage())));
        // initializing timer for kicking player if he/she hasn't logged in a minute
        BlockPos pos = entity.getPosition();
        PlayerDescriptor dc = new PlayerDescriptor(entity, pos);
        descriptors.put(entity, dc);
        scheduler.schedule(() -> {
            if(descriptors.containsKey(entity)) {
                descriptors.remove(entity);
                logged.remove(entity);
                ((EntityPlayerMP) entity).connection.sendPacket(new SPacketDisconnect(new TextComponentString("Wake up! You only have " +  AuthMod.getConfig().getDelay() + " seconds to log in.")));
            }
        }, AuthMod.getConfig().getDelay(), TimeUnit.SECONDS);
    }

    @SubscribeEvent(priority= EventPriority.HIGHEST)
    public static void onLeave(PlayerLoggedOutEvent event){
        logged.remove(event.player);
    }

    @SubscribeEvent
    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        if (descriptors.containsKey(event.player)) {
            BlockPos pos = descriptors.get(event.player).getPosition();
            event.player.setPositionAndUpdate(
                pos.getX(),
                pos.getY(),
                pos.getZ()
            );
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onPlayerEvent(PlayerEvent event) {
        EntityPlayer entity = event.getEntityPlayer();
        if(descriptors.containsKey(entity) && event.isCancelable()) {
            event.setCanceled(true);
            ((EntityPlayerMP) entity).connection.sendPacket(new SPacketChat(new TextComponentString(AuthMod.getConfig().getWelcomeMessage())));
        }
    }

    @SubscribeEvent(priority= EventPriority.HIGHEST)
    public static void onCommand(CommandEvent event){
        String name = event.getCommand().getName();
        if (descriptors.containsKey(event.getSender()) && !(name.equals("register") || name.equals("login") || name.equals("logged?")) && (event.getSender() instanceof EntityPlayer) && event.isCancelable()) {
            event.setCanceled(true);
            ((EntityPlayerMP)event.getSender()).connection.sendPacket(new SPacketChat(new TextComponentString(AuthMod.getConfig().getWelcomeMessage())));
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onChatEvent(ServerChatEvent event) {
        EntityPlayerMP entity = event.getPlayer();
        if (event.isCancelable() && descriptors.containsKey(entity)) {
            event.setCanceled(true);
            event.getPlayer().connection.sendPacket(new SPacketChat(new TextComponentString(AuthMod.getConfig().getWelcomeMessage())));
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onTossEvent(ItemTossEvent event) {
        EntityPlayer entity = event.getPlayer();
        if (event.isCancelable() && descriptors.containsKey(entity)) {
            event.setCanceled(true);
            entity.inventory.addItemStackToInventory(event.getEntityItem().getItem());
            ((EntityPlayerMP)event.getPlayer()).connection.sendPacket(new SPacketChat(new TextComponentString(AuthMod.getConfig().getWelcomeMessage())));
        }
    }

    /*
    This is the list of the different LivingEvents we want to block
    We cannot block every single LivingEvent because of LivingUpdateEvent (defined in LivingEvent)
     */
    private static void handleLivingEvents(LivingEvent event, Entity entity) {
        if (event.getEntity() instanceof EntityPlayer && event.isCancelable() && descriptors.containsKey(entity)) {
            event.setCanceled(true);
            ((EntityPlayerMP) entity).connection.sendPacket(new SPacketChat(new TextComponentString(AuthMod.getConfig().getWelcomeMessage())));
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onLivingAttackEvent(LivingAttackEvent event) {
        handleLivingEvents(event, event.getEntity());
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        handleLivingEvents(event, event.getEntity());
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onLivingEntityUseItemEvent(LivingEntityUseItemEvent event) {
        handleLivingEvents(event, event.getEntity());
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onLivingHealEvent(LivingHealEvent event) {
        handleLivingEvents(event, event.getEntity());
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onLivingHurtEvent(LivingHurtEvent event) {
        handleLivingEvents(event, event.getEntity());
    }

    /* NOT CANCELABLE*/
    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onLivingSetTargetAttackEvent(LivingSetAttackTargetEvent event) {
        if (event.getTarget() instanceof EntityPlayer && descriptors.containsKey(event.getTarget())) {
            ((EntityLiving)event.getEntityLiving()).setAttackTarget(null);
        }
    }

    public static PlayerDescriptor authorizePlayer(EntityPlayer player) {
        logged.put(player, true);
        return descriptors.remove(player);
    }

    public static boolean isLogged(EntityPlayer player) {
        return logged.getOrDefault(player, false);
    }
}
