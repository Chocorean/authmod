package io.chocorean.authmod;

import io.chocorean.authmod.command.LoggedCommand;
import io.chocorean.authmod.command.LoginCommand;
import io.chocorean.authmod.command.RegisterCommand;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mod(AuthMod.MODID)
public class AuthMod {
  public static final String MODID = "authmod";
  static final String NAME = "AuthMod";
  static final String VERSION = "3.2";
  public static final Logger LOGGER = LogManager.getLogger();
  private Handler handler;

  public AuthMod() {
    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    this.handler = new Handler();
    modEventBus.register(this);
    modEventBus.register(AuthModConfig.class);
    MinecraftForge.EVENT_BUS.addListener( this::serverStart );
    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, AuthModConfig.serverSpec);
  }

  private void serverStart(FMLServerStartingEvent event) {
    try {
      GuardInterface guard = this.createGuard(AuthModConfig.get().dataSource.get());
      if(guard != null) {
        this.handler = new Handler();
        boolean identifierRequired = AuthModConfig.get().identifierRequired.get();
        LOGGER.info("Registering /register command");
        RegisterCommand.register(event.getCommandDispatcher(), this.handler, guard, identifierRequired);
        LOGGER.info("Registering /login command");
        LoginCommand.register(event.getCommandDispatcher(), this.handler, guard, identifierRequired);
        LOGGER.info("Registering /logged command");
        LoggedCommand.register(event.getCommandDispatcher(), this.handler);
      } else {
        LOGGER.warn(AuthMod.MODID + " is disabled because guard is NULL");
      }
    } catch(Exception e) { LOGGER.catching(e); }
  }

  private GuardInterface createGuard(AuthModConfig.DataSource ds) throws Exception {
    DataSourceStrategyInterface datasource = null;
    switch (ds) {
      case DATABASE:
        Map<String, String> columns = new HashMap<>();
        DatabaseConfig dbconfig = AuthModConfig.get().database;
        columns.put(DatabaseStrategy.IDENTIFIER_COLUMN, dbconfig.columnIdentifier.get().trim());
        columns.put(DatabaseStrategy.USERNAME_COLUMN, dbconfig.columnUsername.get().trim());
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
        datasource = null;
    }
    if(datasource == null) {
      return null;
    }
    LOGGER.info("Use guard " + datasource);
    return new DataSourceGuard(datasource);
  }

  public static PayloadInterface toPayload(PlayerEntity entity, String ...args) {
    return new Payload(new Player(entity.getDisplayName().getString(), entity.getGameProfile().getId().toString()),
      Arrays.stream(args).filter(Objects::nonNull).toArray(String[]::new));
  }

}
