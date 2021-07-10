package io.chocorean.authmod.event;

import io.chocorean.authmod.config.Config;
import io.chocorean.authmod.core.PlayerDescriptor;
import io.chocorean.authmod.util.text.ServerTranslationTextComponent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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

@Mod.EventBusSubscriber
public class Handler {

  private static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
  private static final Map<EntityPlayer, PlayerDescriptor> descriptors = new HashMap<>();
  private static final Map<EntityPlayer, Boolean> logged = new HashMap<>();

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onJoin(PlayerLoggedInEvent event) {
    EntityPlayer entity = event.player;
    Vec3d pos = entity.getPositionVector();
    PlayerDescriptor descriptor = new PlayerDescriptor(pos.x, pos.y, pos.z);
    descriptors.put(entity, descriptor);
    scheduler.schedule(
      () -> {
        if (descriptors.containsKey(entity)) {
          descriptors.remove(entity);
          logged.remove(entity);
          ((EntityPlayerMP) entity).connection.sendPacket(new SPacketDisconnect(wakeUp()));
        }
      },
      Config.delay,
      TimeUnit.SECONDS
    );
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLeave(PlayerLoggedOutEvent event) {
    logged.remove(event.player);
  }

  @SubscribeEvent
  public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
    if (descriptors.containsKey(event.player)) {
      teleportTo(event.player, descriptors.get(event.player));
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onPlayerEvent(PlayerEvent event) {
    EntityPlayer entity = event.getEntityPlayer();
    if (descriptors.containsKey(entity) && event.isCancelable()) {
      event.setCanceled(true);
      sayWelcome(entity);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onCommand(CommandEvent event) {
    List<String> whitelist = Arrays.asList(Config.allowlist);
    String name = event.getCommand().getName();
    boolean isCommandAllowed = whitelist.contains(name);
    EntityPlayer playerEntity = (EntityPlayer) event.getSender();
    if (descriptors.containsKey(playerEntity) && !isCommandAllowed && event.isCancelable()) {
      event.setCanceled(true);
      sayWelcome(playerEntity);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onChatEvent(ServerChatEvent event) {
    EntityPlayerMP entity = event.getPlayer();
    if (event.isCancelable() && descriptors.containsKey(entity)) {
      event.setCanceled(true);
      sayWelcome(entity);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onTossEvent(ItemTossEvent event) {
    EntityPlayer entity = event.getPlayer();
    if (event.isCancelable() && descriptors.containsKey(entity)) {
      event.setCanceled(true);
      entity.inventory.addItemStackToInventory(event.getEntityItem().getItem());
      sayWelcome(entity);
    }
  }

  /*
  This is the list of the different LivingEvents we want to block
  We cannot block every single LivingEvent because of LivingUpdateEvent (defined in LivingEvent)
   */
  private static void handleLivingEvents(LivingEvent event, Entity entity) {
    if (event.getEntity() instanceof EntityPlayer && event.isCancelable() && descriptors.containsKey(entity)) {
      event.setCanceled(true);
      sayWelcome(entity);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLivingAttackEvent(LivingAttackEvent event) {
    handleLivingEvents(event, event.getEntity());
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLivingDeathEvent(LivingDeathEvent event) {
    handleLivingEvents(event, event.getEntity());
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLivingEntityUseItemEvent(LivingEntityUseItemEvent event) {
    handleLivingEvents(event, event.getEntity());
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLivingHealEvent(LivingHealEvent event) {
    handleLivingEvents(event, event.getEntity());
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLivingHurtEvent(LivingHurtEvent event) {
    handleLivingEvents(event, event.getEntity());
  }

  /* NOT CANCELABLE*/
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLivingSetTargetAttackEvent(LivingSetAttackTargetEvent event) {
    if (event.getTarget() instanceof EntityPlayer && descriptors.containsKey(event.getTarget())) {
      ((EntityLiving) event.getEntityLiving()).setAttackTarget(null);
    }
  }

  public void authorizePlayer(EntityPlayer player) {
    logged.put(player, true);
    descriptors.remove(player);
  }

  public boolean isLogged(EntityPlayer player) {
    return logged.getOrDefault(player, false);
  }

  private static void sayWelcome(Entity playerEntity) {
    playerEntity.sendMessage(new ServerTranslationTextComponent("authmod.welcome"));
  }

  private static ServerTranslationTextComponent wakeUp() {
    return new ServerTranslationTextComponent("authmod.wakeUp", Config.delay);
  }

  private static void teleportTo(EntityPlayer player, PlayerDescriptor pos) {
    ((EntityPlayerMP) player).connection.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
  }
}
