package io.chocorean.authmod.setup;

import com.mojang.brigadier.CommandDispatcher;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.command.*;
import io.chocorean.authmod.config.Config;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.event.Handler;
import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.Logger;

public class CommandsSetup {

  public static final Logger LOGGER = AuthMod.LOGGER;
  private final GuardInterface guard;
  private final Handler handler;

  public CommandsSetup(Handler handler, GuardInterface guard) {
    this.handler = handler;
    this.guard = guard;
  }

  @SubscribeEvent
  public void onCommandsRegister(RegisterCommandsEvent event) {
    LOGGER.info("Register commands");
    this.registerCommands(event);
  }

  private void registerCommands(RegisterCommandsEvent event) {
    if (this.guard != null) {
      registerLoginCommands(Config.enableLogin.get(), event.getDispatcher(), this.guard);
      registerRegisterCommand(Config.enableRegister.get(), event.getDispatcher(), this.guard);
      registerChangePasswordCommand(Config.enableChangePassword.get(), event.getDispatcher(), this.guard);
    } else {
      LOGGER.warn("{} is disabled because guard is NULL", AuthMod.MODID);
    }
  }

  private void registerChangePasswordCommand(
    boolean enabled,
    CommandDispatcher<CommandSource> commandDispatcher,
    GuardInterface guard
  ) {
    if (enabled) {
      LOGGER.info("Registering /changepassword command");
      commandDispatcher.register(new ChangePasswordCommand(handler, guard).getCommandBuilder());
    }
  }

  private void registerRegisterCommand(boolean enabled,
    CommandDispatcher<CommandSource> commandDispatcher, GuardInterface guard) {
    if (enabled) {
      LOGGER.info("Registering /register command");
      RegisterCommand command = new RegisterCommand(handler, guard);
      commandDispatcher.register(command.getCommandBuilder());
    }
  }

  private void registerLoginCommands(boolean enabled, CommandDispatcher<CommandSource> commandDispatcher,
    GuardInterface guard
  ) {
    if (enabled) {
      LOGGER.info("Registering /login command");
      LoginCommand command = new LoginCommand(handler, guard);
      commandDispatcher.register(command.getCommandBuilder());
      LOGGER.info("Registering /logged command");
      commandDispatcher.register(new LoggedCommand(handler).getCommandBuilder());
    }
  }
}
