package io.chocorean.authmod;

import io.chocorean.authmod.command.ExceptionToMessageMapper;
import io.chocorean.authmod.command.LoggedCommand;
import io.chocorean.authmod.command.LoginCommand;
import io.chocorean.authmod.command.RegisterCommand;
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
import io.chocorean.authmod.util.text.ServerLanguageMap;
import net.minecraft.util.text.LanguageMap;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.VillagerTradingManager;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.items.CapabilityItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;


@Mod(AuthMod.MODID)
public class AuthMod {

  public static final String MODID = "authmod";
  public static final Logger LOGGER = LogManager.getLogger();
  private Handler handler;
  private GuardInterface guard;

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
      this.guard = this.createGuard(AuthModConfig.get().dataSource.get());
      this.handler = new Handler();
      boolean identifierRequired = AuthModConfig.get().identifierRequired.get();
      LOGGER.info("Registering /register command");
      RegisterCommand.register(event.getCommandDispatcher(), this.handler, this.guard, identifierRequired);
      LOGGER.info("Registering /login command");
      LoginCommand.register(event.getCommandDispatcher(), this.handler, this.guard, identifierRequired);
      LOGGER.info("Registering /logged command");
      LoggedCommand.register(event.getCommandDispatcher(), this.handler);
    } catch(Exception e) { LOGGER.catching(e); }
  }

  private GuardInterface createGuard(AuthModConfig.DataSource ds) throws Exception {
    DataSourceStrategyInterface datasource = null;
    switch (ds) {
      case DATABASE:
        Map<String, String> columns = new HashMap<>();
        DatabaseConfig dbconfig = AuthModConfig.get().database;
        columns.put(DatabaseStrategy.IDENTIFIER_COLUMN, dbconfig.columnIdentifier.get());
        columns.put(DatabaseStrategy.USERNAME_COLUMN, dbconfig.columnUsername.get());
        columns.put(DatabaseStrategy.PASSWORD_COLUMN, dbconfig.columnPassword.get());
        columns.put(DatabaseStrategy.BANNED_COLUMN, dbconfig.columnBan.get());
        ConnectionFactoryInterface connectionFactory = new ConnectionFactory(
          dbconfig.dialect.get(),
          dbconfig.host.get(),
          dbconfig.port.get(),
          dbconfig.database.get(),
          dbconfig.user.get(),
          dbconfig.password.get());
        datasource = new DatabaseStrategy(dbconfig.table.get(), connectionFactory, columns, new BcryptPasswordHash());
        break;
      case FILE:
        datasource = new FileDataSourceStrategy(Paths.get(FMLPaths.CONFIGDIR.get().toString(), MODID + ".csv").toFile());
    }
    if(datasource == null) {
      return null;
    }
    LOGGER.info("Use " + datasource);
    return new DataSourceGuard(datasource);
  }

}
