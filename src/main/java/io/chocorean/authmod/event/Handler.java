package io.chocorean.authmod.event;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.config.Config;
import io.chocorean.authmod.core.PlayerDescriptor;
import io.chocorean.authmod.util.text.ServerTranslationTextComponent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SDisconnectPacket;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class Handler {

  private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
  private final Map<PlayerEntity, PlayerDescriptor> descriptors = new HashMap<>();
  private final Map<PlayerEntity, Boolean> logged = new HashMap<>();

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
    PlayerEntity entity = event.getPlayer();
    // initializing timer for kicking player if he/she hasn't logged
    Vector3d pos = entity.position();
    PlayerDescriptor dc = new PlayerDescriptor(pos.x, pos.y, pos.z);
    descriptors.put(entity, dc);
    scheduler.schedule(
      () -> {
        if (descriptors.containsKey(entity)) {
          descriptors.remove(entity);
          logged.remove(entity);
          ((ServerPlayerEntity) event.getPlayer()).connection.send(new SDisconnectPacket(wakeUp()));
        }
      },
      Config.delay.get(),
      TimeUnit.SECONDS
    );
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onLeave(PlayerEvent.PlayerLoggedOutEvent event) {
    logged.remove(event.getPlayer());
  }

  @SubscribeEvent
  public void onPlayerInteractEvent(PlayerInteractEvent event) {
    PlayerEntity player = event.getPlayer();
    if (descriptors.containsKey(player) && event.getSide() == LogicalSide.SERVER) {
      event.setCanceled(true);
      teleportTo(player, descriptors.get(player));
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onPlayerEvent(PlayerEvent event) {
    PlayerEntity entity = event.getPlayer();
    if (descriptors.containsKey(entity) && event.isCancelable()) {
      event.setCanceled(true);
      teleportTo(entity, descriptors.get(entity));
      sayWelcome(entity);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onCommand(CommandEvent event) throws CommandSyntaxException {
    CommandSource source = event.getParseResults().getContext().getSource();
    try {
      PlayerEntity playerEntity = source.getPlayerOrException();
      List<? extends String> whitelist = Config.commandWhitelist.get();
      String name = event.getParseResults().getContext().getNodes().get(0).getNode().getName();
      boolean isCommandAllowed = whitelist.contains(name);
      if (descriptors.containsKey(playerEntity) && !isCommandAllowed && event.isCancelable()) {
        event.setCanceled(true);
        event
          .getParseResults()
          .getContext().getSource()
          .sendSuccess(new ServerTranslationTextComponent("authmod.welcome"), false);
      }
    } catch (CommandSyntaxException e) {
      // raised when command comes from non-player entity
      return;
    }
  }

  @SubscribeEvent
  public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
    if (descriptors.containsKey(event.player) && event.side == LogicalSide.SERVER) {
      teleportTo(event.player, descriptors.get(event.player));
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onChatEvent(ServerChatEvent event) {
    PlayerEntity entity = event.getPlayer();
    if (descriptors.containsKey(entity)) {
      this.sayWelcome(entity);
      event.setCanceled(true);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onTossEvent(ItemTossEvent event) {
    PlayerEntity entity = event.getPlayer();
    if (event.isCancelable() && descriptors.containsKey(entity)) {
      event.setCanceled(true);
      entity.inventory.add(event.getEntityItem().getItem());
      sayWelcome(entity);
    }
  }

  private void handleLivingEvents(LivingEvent event, Entity entity) {
    if (event.getEntity() instanceof PlayerEntity && event.isCancelable() && descriptors.containsKey(entity)) {
      event.setCanceled(true);
      PlayerDescriptor desc = descriptors.get(event.getEntity());
      event.getEntityLiving().teleportTo(desc.getX(), desc.getY(), desc.getZ());
      sayWelcome(entity);
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onLivingAttackEvent(LivingAttackEvent event) {
    handleLivingEvents(event, event.getEntity());
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onLivingDeathEvent(LivingDeathEvent event) {
    handleLivingEvents(event, event.getEntity());
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onLivingEntityUseItemEvent(LivingEntityUseItemEvent event) {
    handleLivingEvents(event, event.getEntity());
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onLivingHealEvent(LivingHealEvent event) {
    handleLivingEvents(event, event.getEntity());
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onLivingHurtEvent(LivingHurtEvent event) {
    handleLivingEvents(event, event.getEntity());
  }

  public void authorizePlayer(PlayerEntity player) {
    logged.put(player, true);
    descriptors.remove(player);
  }

  public boolean isLogged(PlayerEntity player) {
    return logged.getOrDefault(player, false);
  }

  private void sayWelcome(Entity playerEntity) {
    playerEntity.sendMessage(new ServerTranslationTextComponent("authmod.welcome"), playerEntity.getUUID());
  }

  private static ServerTranslationTextComponent wakeUp() {
    return new ServerTranslationTextComponent("authmod.wakeUp", Config.delay.get());
  }

  private void teleportTo(PlayerEntity player, PlayerDescriptor pos) {
    player.setPos(pos.getX(), pos.getY(), pos.getZ());
    player.teleportTo(pos.getX(), pos.getY(), pos.getZ());
  }
}
