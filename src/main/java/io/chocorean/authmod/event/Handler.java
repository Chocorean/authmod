package io.chocorean.authmod.event;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.config.AuthModConfig;
import io.chocorean.authmod.core.PlayerDescriptor;
import io.chocorean.authmod.core.PlayerPos;
import io.chocorean.authmod.util.text.ServerLanguageMap;
import io.chocorean.authmod.util.text.ServerTranslationTextComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SDisconnectPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber
public class Handler {
  private static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
  private static final Map<PlayerEntity, PlayerDescriptor> descriptors = new HashMap<>();
  private static final Map<PlayerEntity, Boolean> logged = new HashMap<>();
  private static final String WELCOME = ServerLanguageMap.getInstance().translateKey("welcome");
  private static final String WAKE_UP = String.format(ServerLanguageMap.getInstance().translateKey("wakeUp"), AuthModConfig.get().delay.get());

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
    PlayerEntity entity = event.getPlayer();
    // initializing timer for kicking player if he/she hasn't logged
    BlockPos pos = entity.getPosition();
    float yaw = entity.rotationYaw, pitch = entity.rotationPitch;
    PlayerPos pp = new PlayerPos(pos, yaw, pitch);
    PlayerDescriptor dc = new PlayerDescriptor(entity, pp);
    descriptors.put(entity, dc);
    scheduler.schedule(
        () -> {
          if (descriptors.containsKey(entity)) {
            descriptors.remove(entity);
            logged.remove(entity);
            ((ServerPlayerEntity) event.getPlayer()).connection.sendPacket(new SDisconnectPacket(new StringTextComponent(WAKE_UP)));
          }
        }, AuthModConfig.get().delay.get(), TimeUnit.SECONDS);
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLeave(PlayerEvent.PlayerLoggedOutEvent event) {
    logged.remove(event.getPlayer());
  }

  // TODO it's not working
  @SubscribeEvent
  public static void onPlayerInteractEvent(PlayerInteractEvent event) {
    PlayerEntity player = event.getPlayer();
    if (descriptors.containsKey(player)) {
      PlayerPos pp = descriptors.get(player).getPosition();
      BlockPos pos = pp.getPosition();
      player.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), pp.getYaw(), pp.getPitch());
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onPlayerEvent(PlayerEvent event) {
    PlayerEntity entity = event.getPlayer();
    if (descriptors.containsKey(entity) && event.isCancelable()) {
      event.setCanceled(true);
      entity.sendMessage(new StringTextComponent(WELCOME));
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onCommand(CommandEvent event) {
    try {
      PlayerEntity playerEntity = event.getParseResults().getContext().getSource().asPlayer();
      String name = event.getParseResults().getContext().getNodes().get(0).getNode().getName();
      if (descriptors.containsKey(playerEntity)
        && !(name.equals("register") || name.equals("login") || name.equals("logged"))
        && event.isCancelable()) {
        event.setCanceled(true);
        event.getParseResults().getContext().getSource().sendFeedback(new StringTextComponent(WELCOME), false);
      }
    } catch (CommandSyntaxException e) {
      AuthMod.LOGGER.catching(e);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onChatEvent(ServerChatEvent event) {
    PlayerEntity entity = event.getPlayer();
    if (event.isCancelable() && descriptors.containsKey(entity)) {
      event.setCanceled(true);
      event.getPlayer().sendMessage(new StringTextComponent(WELCOME));
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onTossEvent(ItemTossEvent event) {
    PlayerEntity entity = event.getPlayer();
    if (event.isCancelable() && descriptors.containsKey(entity)) {
      event.setCanceled(true);
      entity.inventory.addItemStackToInventory(event.getEntityItem().getItem());
      event.getPlayer().sendMessage(new StringTextComponent(WELCOME));
    }
  }

  private static void handleLivingEvents(LivingEvent event, Entity entity) {
    if (event.getEntity() instanceof PlayerEntity && event.isCancelable() && descriptors.containsKey(entity)) {
      event.setCanceled(true);
      entity.sendMessage(new StringTextComponent(WELCOME));
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
    if (event.getTarget() instanceof PlayerEntity && descriptors.containsKey(event.getTarget())) {
      event.getEntityLiving().setRevengeTarget(null);
    }
  }

  public void authorizePlayer(PlayerEntity player) {
    logged.put(player, true);
    descriptors.remove(player);
  }

  public boolean isLogged(PlayerEntity player) {
    return logged.getOrDefault(player, false);
  }

}
