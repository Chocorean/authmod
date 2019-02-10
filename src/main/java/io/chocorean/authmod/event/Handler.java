package io.chocorean.authmod.event;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.config.AuthModConfig;
import io.chocorean.authmod.model.PlayerDescriptor;
import io.chocorean.authmod.model.PlayerPos;
import java.util.HashMap;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
public class Handler {
  private static final Logger LOGGER = AuthMod.LOGGER;
  private static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);
  private static final Map<EntityPlayer, PlayerDescriptor> descriptors = new HashMap<>();
  private static final Map<EntityPlayer, Boolean> logged = new HashMap<>();
  private static final String welcome = new TextComponentTranslation("welcome").getFormattedText();
  private static final String wakeUp =
      new TextComponentTranslation("delay", AuthModConfig.delay).getFormattedText();

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onJoin(PlayerLoggedInEvent event) {
    EntityPlayer entity = event.player;
    // initializing timer for kicking player if he/she hasn't logged in a minute
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
            ((EntityPlayerMP) entity)
                .connection.sendPacket(new SPacketDisconnect(new TextComponentString(wakeUp)));
          }
        },
        AuthModConfig.delay,
        TimeUnit.SECONDS);
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onLeave(PlayerLoggedOutEvent event) {
    logged.remove(event.player);
  }

  @SubscribeEvent
  public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
    if (descriptors.containsKey(event.player)) {
      PlayerPos pp = descriptors.get(event.player).getPosition();
      BlockPos pos = pp.getPosition();
      ((EntityPlayerMP) event.player)
          .connection.setPlayerLocation(
              pos.getX(), pos.getY(), pos.getZ(), pp.getYaw(), pp.getPitch());
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onPlayerEvent(PlayerEvent event) {
    EntityPlayer entity = event.getEntityPlayer();
    if (descriptors.containsKey(entity) && event.isCancelable()) {
      event.setCanceled(true);
      entity.sendMessage(new TextComponentString(welcome));
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onCommand(CommandEvent event) {
    String name = event.getCommand().getName();
    if (descriptors.containsKey(event.getSender())
        && !(name.equals("register") || name.equals("login") || name.equals("logged"))
        && event.getSender() instanceof EntityPlayer
        && event.isCancelable()) {
      event.setCanceled(true);
      event.getSender().sendMessage(new TextComponentString(welcome));
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onChatEvent(ServerChatEvent event) {
    EntityPlayerMP entity = event.getPlayer();
    if (event.isCancelable() && descriptors.containsKey(entity)) {
      event.setCanceled(true);
      event.getPlayer().sendMessage(new TextComponentString(welcome));
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onTossEvent(ItemTossEvent event) {
    EntityPlayer entity = event.getPlayer();
    if (event.isCancelable() && descriptors.containsKey(entity)) {
      event.setCanceled(true);
      entity.inventory.addItemStackToInventory(event.getEntityItem().getItem());
      event.getPlayer().sendMessage(new TextComponentString(welcome));
    }
  }

  /*
  This is the list of the different LivingEvents we want to block
  We cannot block every single LivingEvent because of LivingUpdateEvent (defined in LivingEvent)
   */
  private static void handleLivingEvents(LivingEvent event, Entity entity) {
    if (event.getEntity() instanceof EntityPlayer
        && event.isCancelable()
        && descriptors.containsKey(entity)) {
      event.setCanceled(true);
      entity.sendMessage(new TextComponentString(welcome));
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

  @SubscribeEvent
  public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals(AuthMod.MODID)) {
      ConfigManager.sync(AuthMod.MODID, Config.Type.INSTANCE);
    }
  }

  public ITextComponent getMessage(String key, Object... args) {
    return new TextComponentString(
        new TextComponentTranslation(key, args).getUnformattedComponentText());
  }
}
