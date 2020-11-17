package io.chocorean.authmod;

import com.mojang.brigadier.CommandDispatcher;
import io.chocorean.authmod.command.*;
import io.chocorean.authmod.config.AuthModConfig;
import io.chocorean.authmod.config.DatabaseConfig;
import io.chocorean.authmod.core.DataSourceGuard;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.datasource.BcryptPasswordHash;
import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.datasource.DatabaseStrategy;
import io.chocorean.authmod.core.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.core.datasource.db.ConnectionFactory;
import io.chocorean.authmod.core.datasource.db.ConnectionFactoryInterface;
import io.chocorean.authmod.event.Handler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Mod(AuthMod.MODID)
public class AuthMod {
  public static final String MODID = "authmod";
  static final String NAME = "AuthMod";
  static final String VERSION = "4.0";
  private static final String VERSION_URL = "https://raw.githubusercontent.com/Chocorean/authmod/master/VERSION";
  public static final Logger LOGGER = LogManager.getLogger(NAME);
  private Handler handler;

  public AuthMod() {
    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    final ModLoadingContext modLoadingContext = ModLoadingContext.get();

    modLoadingContext.registerConfig(ModConfig.Type.SERVER, AuthModConfig.serverSpec);
    MinecraftForge.EVENT_BUS.register(this);
  }


  @SubscribeEvent
  public void onServerStarting(FMLServerStartingEvent event) {
    LOGGER.info("{} {}", NAME, VERSION);
  }


  @SubscribeEvent
  public void onCommandsRegister(RegisterCommandsEvent event) {
    LOGGER.info("Register commands");
    AuthmodCommands.registerCommands(event);
  }


  private void checkForUpdates() {
    try (BufferedInputStream in = new BufferedInputStream(new URL(VERSION_URL).openStream())) {
      byte[] dataBuffer = new byte[1024];
      StringBuilder sb = new StringBuilder();
      while ((in.read(dataBuffer, 0, 1024)) != -1) {
        sb.append(new String(dataBuffer));
      }
      String pattern = "[^a-zA-Z0-9.]";
      String version = sb.toString().replaceAll(pattern, "");
      if (!version.contentEquals(VERSION))
        LOGGER.warn("An update is available! '{}' -> '{}'", VERSION, version);
    } catch (IOException e) {
      LOGGER.catching(e);
    }
  }

}
