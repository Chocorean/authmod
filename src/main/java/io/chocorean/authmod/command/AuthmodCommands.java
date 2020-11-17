package io.chocorean.authmod.command;

import com.mojang.brigadier.CommandDispatcher;
import io.chocorean.authmod.AuthMod;
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
import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AuthmodCommands {

  public static final Logger LOGGER = AuthMod.LOGGER;
  private static Handler handler;

  public static void registerCommands(RegisterCommandsEvent event) {
    if (AuthModConfig.SERVER.enableAuthmod()) {
      try {
        handler = new Handler();
        boolean identifierRequired = AuthModConfig.SERVER.identifierRequired.get();
        GuardInterface guard = createGuard(AuthModConfig.SERVER.dataSource.get(), identifierRequired);
        if(guard != null) {
          registerLoginCommands(AuthModConfig.SERVER.enableLogin.get(), identifierRequired, event.getDispatcher() , guard);
          registerRegisterCommand(AuthModConfig.SERVER.enableRegister.get(), identifierRequired, event.getDispatcher(), guard);
          registerChangePasswordCommand(AuthModConfig.SERVER.enableRegister.get(), event.getDispatcher(), guard);
        } else {
          LOGGER.warn("{} is disabled because guard is NULL", AuthMod.MODID);
        }
      } catch(Exception e) { LOGGER.catching(e); }
    }
  }

  private static GuardInterface createGuard(AuthModConfig.DataSource ds, boolean identifierRequired) throws IOException, ClassNotFoundException, SQLException, SQLException {
    DataSourceStrategyInterface datasource;
    switch (ds) {
      case DATABASE:
        Map<String, String> columns = new HashMap<>();
        DatabaseConfig dbconfig = AuthModConfig.SERVER.database;
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
        datasource = new FileDataSourceStrategy(Paths.get(FMLPaths.CONFIGDIR.get().toString(), AuthMod.MODID + ".csv").toFile());
        break;
      case NONE:
      default:
        return null;
    }
    LOGGER.info("Use guard {}", datasource);
    return new DataSourceGuard(datasource, identifierRequired);
  }

  private static void registerChangePasswordCommand(boolean enabled, CommandDispatcher<CommandSource> commandDispatcher, GuardInterface guard) {
    if (enabled) {
      LOGGER.info("Registering /changepassword command");
      commandDispatcher.register(new ChangePasswordCommand(handler, guard).getCommandBuilder());
    }
  }

  private static void registerRegisterCommand(boolean enabled, boolean identifierRequired, CommandDispatcher<CommandSource> commandDispatcher, GuardInterface guard) {
    if (enabled) {
      LOGGER.info("Registering /register command");
      RegisterCommand command = new RegisterCommand(handler, guard);
      if (identifierRequired) {
        command = new RegisterWithIdentifierCommand(handler, guard);
      }
      commandDispatcher.register(command.getCommandBuilder());
    }
  }

  private static void registerLoginCommands(boolean enabled, boolean identifierRequired, CommandDispatcher<CommandSource> commandDispatcher, GuardInterface guard) {
    if (enabled) {
      LOGGER.info("Registering /login command");
      LoginCommand command = new LoginCommand(handler, guard);
      if (identifierRequired) {
        command = new LoginWithIdentifierCommand(handler, guard);
      }
      commandDispatcher.register(command.getCommandBuilder());
      LOGGER.info("Registering /logged command");
      commandDispatcher.register(new LoggedCommand(handler).getCommandBuilder());
    }
  }
}
