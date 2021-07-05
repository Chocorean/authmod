package io.chocorean.authmod;

import io.chocorean.authmod.command.ChangePasswordCommand;
import io.chocorean.authmod.command.LoggedCommand;
import io.chocorean.authmod.command.LoginCommand;
import io.chocorean.authmod.command.RegisterCommand;
import io.chocorean.authmod.config.AuthModConfig;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.guard.datasource.DatabaseSourceStrategy;
import io.chocorean.authmod.guard.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.guard.datasource.IDataSourceStrategy;
import io.chocorean.authmod.guard.datasource.db.ConnectionFactory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;

import static io.chocorean.authmod.config.AuthModConfig.enableAuthentication;
import static io.chocorean.authmod.config.AuthModConfig.enableRegistration;
import static io.chocorean.authmod.config.AuthModConfig.enableChangePassword;

@Mod(modid = AuthMod.MODID, name = AuthMod.NAME, version = AuthMod.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class AuthMod {
  public static final String MODID = "authmod";
  static final String NAME = "AuthMod";
  static final String VERSION = "3.2";
  public static Logger LOGGER = FMLLog.log;

  private Handler handler;
  private IDataSourceStrategy dataSourceStrategy;


  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) throws Exception {
    AuthMod.LOGGER = event.getModLog();
    switch (AuthModConfig.dataSourceStrategy) {
      case DATABASE:
        this.dataSourceStrategy =
            new DatabaseSourceStrategy(
                AuthModConfig.database.table,
                new ConnectionFactory(
                    AuthModConfig.database.dialect,
                    AuthModConfig.database.host,
                    AuthModConfig.database.port,
                    AuthModConfig.database.database,
                    AuthModConfig.database.user,
                    AuthModConfig.database.password));
        LOGGER.info("Now using DatabaseSourceStrategy.");
        break;
      case FILE:
        this.dataSourceStrategy =
            new FileDataSourceStrategy(
                Paths.get(
                        event.getModConfigurationDirectory().getAbsolutePath(),
                        MODID + "_players.csv")
                    .toFile());
        LOGGER.info("Now using FileDataSourceStrategy.");
        break;
      default:
        this.dataSourceStrategy = null;
        LOGGER.info("Unknown guard strategy selected. Nothing will happen.");
    }
  }

  @Mod.EventHandler
  public void serverStarting(FMLServerStartingEvent event) {
    if (AuthModConfig.dataSourceStrategy != null) {
      this.handler = new Handler(); 
      if (enableAuthentication) {
        LOGGER.info("Registering AuthMod event handler");
        MinecraftForge.EVENT_BUS.register(this.handler);
        LOGGER.info("Registering AuthMod /login command");
        event.registerServerCommand(new LoginCommand(this.handler, this.dataSourceStrategy, AuthModConfig.emailRequired));
        LOGGER.info("Registering AuthMod /logged command");
        event.registerServerCommand(new LoggedCommand(this.handler));
      }
      if (enableRegistration) {
        LOGGER.info("Registering AuthMod /register command");
        event.registerServerCommand(new RegisterCommand(this.handler, this.dataSourceStrategy, AuthModConfig.emailRequired));
      }
      if (enableChangePassword) {
          LOGGER.info("Registering AuthMod /changepassword command");
          event.registerServerCommand(new ChangePasswordCommand(this.handler, this.dataSourceStrategy));
      }
    }
  }
}
