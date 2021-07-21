package io.chocorean.authmod.setup;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.command.ChangePasswordCommand;
import io.chocorean.authmod.command.LoggedCommand;
import io.chocorean.authmod.command.LoginCommand;
import io.chocorean.authmod.command.RegisterCommand;
import io.chocorean.authmod.config.Config;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.event.Handler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

public class CommandsSetup {

  public static final Logger LOGGER = AuthMod.LOGGER;
  private final GuardInterface guard;
  private final Handler handler;

  public CommandsSetup(Handler handler, GuardInterface guard) {
    this.handler = handler;
    this.guard = guard;
  }

  public void registerCommands(FMLServerStartingEvent event) {
    if (this.guard != null) {
      registerLoginCommands(event);
      registerRegisterCommand(event);
      registerChangePasswordCommand(event);
    } else {
      LOGGER.warn("{} is disabled because guard is NULL", AuthMod.MODID);
    }
  }

  private void registerChangePasswordCommand(FMLServerStartingEvent event) {
    if (Config.enableChangePassword) {
      LOGGER.info("Registering /changepassword command");
      event.registerServerCommand(new ChangePasswordCommand(this.handler, this.guard));
    }
  }

  private void registerRegisterCommand(FMLServerStartingEvent event) {
    if (Config.enableRegister) {
      LOGGER.info("Registering /register command");
      RegisterCommand command = new RegisterCommand(this.handler, this.guard);
      event.registerServerCommand(command);
    }
  }

  private void registerLoginCommands(FMLServerStartingEvent event) {
    if (Config.enableLogin) {
      LOGGER.info("Registering /login command");
      LoginCommand command = new LoginCommand(this.handler, this.guard);
      event.registerServerCommand(command);
      LOGGER.info("Registering /logged command");
      event.registerServerCommand(new LoggedCommand(this.handler));
    }
  }
}
