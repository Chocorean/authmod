package io.chocorean.authmod;

import com.mojang.brigadier.CommandDispatcher;
import io.chocorean.authmod.command.*;
import io.chocorean.authmod.config.AuthModConfig;
import io.chocorean.authmod.config.DatabaseConfig;
import io.chocorean.authmod.core.*;
import io.chocorean.authmod.core.datasource.BcryptPasswordHash;
import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.datasource.DatabaseStrategy;
import io.chocorean.authmod.core.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.core.datasource.db.ConnectionFactory;
import io.chocorean.authmod.core.datasource.db.ConnectionFactoryInterface;
import io.chocorean.authmod.event.Handler;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    modEventBus.register(this);
    modEventBus.register(AuthModConfig.class);
    MinecraftForge.EVENT_BUS.addListener( this::serverStart );
    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, AuthModConfig.serverSpec);
    LOGGER.info("%s %s", NAME, VERSION);
    this.checkForUpdates();
  }

  private void serverStart(FMLServerStartingEvent event) {
    if (FMLEnvironment.dist.isDedicatedServer() && AuthModConfig.get().enableAuthmod()) {
      try {
        boolean identifierRequired = AuthModConfig.get().identifierRequired.get();
        GuardInterface guard = this.createGuard(AuthModConfig.get().dataSource.get(), identifierRequired);
        this.handler = new Handler();
        if(guard != null) {
          this.registerLoginCommands(AuthModConfig.get().enableLogin.get(), identifierRequired, event.getCommandDispatcher(), guard);
          this.registerRegisterCommand(AuthModConfig.get().enableRegister.get(), identifierRequired, event.getCommandDispatcher(), guard);
          this.registerChangePasswordCommand(AuthModConfig.get().enableRegister.get(), event.getCommandDispatcher(), guard);
        } else {
          LOGGER.warn("%s is disabled because guard is NULL", AuthMod.MODID);
        }
      } catch(Exception e) { LOGGER.catching(e); }
    }

  }

  private GuardInterface createGuard(AuthModConfig.DataSource ds, boolean identifierRequired) throws IOException, ClassNotFoundException, SQLException {
    DataSourceStrategyInterface datasource;
    switch (ds) {
      case DATABASE:
        Map<String, String> columns = new HashMap<>();
        DatabaseConfig dbconfig = AuthModConfig.get().database;
        columns.put(DatabaseStrategy.IDENTIFIER_COLUMN, dbconfig.columnIdentifier.get().trim());
        columns.put(DatabaseStrategy.USERNAME_COLUMN, dbconfig.columnUsername.get().trim());
        columns.put(DatabaseStrategy.UUID_COLUMN, dbconfig.columnUuid.get().trim());
        columns.put(DatabaseStrategy.PASSWORD_COLUMN, dbconfig.columnPassword.get().trim());
        columns.put(DatabaseStrategy.BANNED_COLUMN, dbconfig.columnBan.get().trim());
        ConnectionFactoryInterface connectionFactory = new ConnectionFactory(
                dbconfig.dialect.get().trim(),
                dbconfig.host.get().trim(),
                dbconfig.port.get(),
                dbconfig.database.get().trim(),
                dbconfig.user.get().trim(),
                dbconfig.password.get(),
                dbconfig.driver.get());
        datasource = new DatabaseStrategy(dbconfig.table.get().trim(), connectionFactory, columns, new BcryptPasswordHash());
        break;
      case FILE:
        datasource = new FileDataSourceStrategy(Paths.get(FMLPaths.CONFIGDIR.get().toString(), MODID + ".csv").toFile());
        break;
      case NONE:
      default:
        return null;
    }
    LOGGER.info("Use guard %s", datasource);
    return new DataSourceGuard(datasource, identifierRequired);
  }

  private void checkForUpdates() {
    try (BufferedInputStream in = new BufferedInputStream(new URL(VERSION_URL).openStream())) {
      byte[] dataBuffer = new byte[1024];
      StringBuilder sb = new StringBuilder();
      while ((in.read(dataBuffer, 0, 1024)) != -1) {
        sb.append(new String(dataBuffer));
      }
      String pattern = "[^a-zA-Z0-9.]";
      String version = new StringBuilder(sb.toString().replaceAll(pattern, "")).toString();
      if (!version.contentEquals(VERSION))
        LOGGER.warn("An update is available! '%s' -> '%s'", VERSION, version);
    } catch (Exception e) {
      LOGGER.catching(e);
    }
  }

  public static PayloadInterface toPayload(PlayerEntity entity, String ...args) {
    // TODO when offline, UUID is not the mojang UUID
    String uuid = PlayerEntity.getUUID(entity.getGameProfile()).toString();
    return new Payload(new Player(entity.getDisplayName().getString(), uuid),
            Arrays.stream(args).filter(Objects::nonNull).toArray(String[]::new));
  }

  private void registerChangePasswordCommand(boolean enabled, CommandDispatcher<CommandSource> commandDispatcher, GuardInterface guard) {
    if (enabled) {
      LOGGER.info("Registering /changepassword command");
      commandDispatcher.register(new ChangePasswordCommand(this.handler, guard).getCommandBuilder());
    }
  }

  private void registerRegisterCommand(boolean enabled, boolean identifierRequired, CommandDispatcher<CommandSource> commandDispatcher, GuardInterface guard) {
    if (enabled) {
      LOGGER.info("Registering /register command");
      RegisterCommand command = new RegisterCommand(this.handler, guard);
      if (identifierRequired) {
        command = new RegisterWithIdentifierCommand(this.handler, guard);
      }
      commandDispatcher.register(command.getCommandBuilder());
    }
  }

  private void registerLoginCommands(boolean enabled, boolean identifierRequired, CommandDispatcher<CommandSource> commandDispatcher, GuardInterface guard) {
    if (enabled) {
      LOGGER.info("Registering /login command");
      LoginCommand command = new LoginCommand(this.handler, guard);
      if (identifierRequired) {
        command = new LoginWithIdentifierCommand(this.handler, guard);
      }
      commandDispatcher.register(command.getCommandBuilder());
      LOGGER.info("Registering /logged command");
      commandDispatcher.register(new LoggedCommand(this.handler).getCommandBuilder());
    }
  }
}
