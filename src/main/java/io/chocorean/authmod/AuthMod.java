package io.chocorean.authmod;

import io.chocorean.authmod.command.ExceptionToMessageMapper;
import io.chocorean.authmod.command.LoggedCommand;
import io.chocorean.authmod.command.LoginCommand;
import io.chocorean.authmod.command.RegisterCommand;
import io.chocorean.authmod.config.AuthModConfig;
import io.chocorean.authmod.core.DataSourceGuard;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.datasource.BcryptPasswordHash;
import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.datasource.DatabaseStrategy;
import io.chocorean.authmod.core.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.core.datasource.db.ConnectionFactory;
import io.chocorean.authmod.core.datasource.db.ConnectionFactoryInterface;
import io.chocorean.authmod.event.Handler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static io.chocorean.authmod.config.AuthModConfig.enableAuthentication;
import static io.chocorean.authmod.config.AuthModConfig.enableRegistration;

@Mod(modid = AuthMod.MODID, name = AuthMod.NAME, version = AuthMod.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class AuthMod {

  public static final String MODID = "authmod";
  static final String NAME = "AuthMod";
  static final String VERSION = "3.1";
  public static Logger LOGGER = FMLLog.log;
  private GuardInterface guard;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) throws Exception {
    AuthMod.LOGGER = event.getModLog();
    ExceptionToMessageMapper.init();
    DataSourceStrategyInterface datasource = new FileDataSourceStrategy(Paths.get(event.getModConfigurationDirectory().getAbsolutePath(), MODID + "_players.csv").toFile());
    switch (AuthModConfig.dataSourceStrategy) {
      case DATABASE:
        Map<String, String> columns = new HashMap<>();
        columns.put(DatabaseStrategy.IDENTIFIER_COLUMN, AuthModConfig.database.identifierField);
        columns.put(DatabaseStrategy.USERNAME_COLUMN, AuthModConfig.database.usernameField);
        columns.put(DatabaseStrategy.PASSWORD_COLUMN, AuthModConfig.database.passwordField);
        columns.put(DatabaseStrategy.BANNED_COLUMN, AuthModConfig.database.bannedField);
        ConnectionFactoryInterface connectionFactory = new ConnectionFactory(
        AuthModConfig.database.dialect,
        AuthModConfig.database.host,
        AuthModConfig.database.port,
        AuthModConfig.database.database,
        AuthModConfig.database.user,
        AuthModConfig.database.password);
        datasource = new DatabaseStrategy(AuthModConfig.database.table, connectionFactory, columns, new BcryptPasswordHash());
        LOGGER.info("Now using DatabaseSourceStrategy.");
        break;
      case FILE:
        LOGGER.info("Now using FileDataSourceStrategy.");
        break;
      default:
        LOGGER.info("Unknown guard strategy selected. Nothing will happen, using FileDataSourceStrategy by default");
    }
    this.guard = new DataSourceGuard(datasource, AuthModConfig.identifierRequired);
  }

  @Mod.EventHandler
  public void serverStarting(FMLServerStartingEvent event) {
    if (AuthModConfig.dataSourceStrategy != null) {
      Handler handler = new Handler();
      if (enableAuthentication) {
        LOGGER.info("Registering AuthMod event handler");
        MinecraftForge.EVENT_BUS.register(handler);
        LOGGER.info("Registering AuthMod /login command");
        event.registerServerCommand(new LoginCommand(handler, this.guard));
        LOGGER.info("Registering AuthMod /logged command");
        event.registerServerCommand(new LoggedCommand(handler));
      }
      if (enableRegistration) {
        LOGGER.info("Registering AuthMod /register command");
        event.registerServerCommand(new RegisterCommand(handler, guard));
      }
    }
  }
}
