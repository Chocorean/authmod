package io.chocorean.authmod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.*;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.util.text.ServerTranslationTextComponent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

public class RegisterCommand implements CommandInterface, Command<CommandSource> {

  protected final Handler handler;
  protected final GuardInterface guard;

  public RegisterCommand(Handler handler, GuardInterface guard) {
    this.handler = handler;
    this.guard = guard;
  }

  public LiteralArgumentBuilder<CommandSource> getCommandBuilder() {
    return Commands.literal("register").then(this.getParameters());
  }

  public RequiredArgumentBuilder<CommandSource, String> getParameters() {
    return Commands
      .argument("password", StringArgumentType.string())
      .then(Commands.argument("confirmation", StringArgumentType.string()).executes(this));
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    return execute(
      context.getSource(),
      this.handler,
      this.guard,
      CommandInterface.toPayload(
        context.getSource().getPlayerOrException(),
        StringArgumentType.getString(context, "password"),
        StringArgumentType.getString(context, "confirmation")
      )
    );
  }

  /**
   * @return 1 if something goes wrong, 0 otherwise.
   */
  public static int execute(CommandSource source, Handler handler, GuardInterface guard, PayloadInterface payload) {
    try {
      PlayerEntity player = source.getPlayerOrException();
      if (guard.register(payload) && !handler.isLogged(source.getPlayerOrException())) {
        handler.authorizePlayer(player);
        source.sendSuccess(new ServerTranslationTextComponent("authmod.register.success"), true);
      }
      return 0;
    } catch (AuthmodError e) {
      source.sendFailure(new ServerTranslationTextComponent(e.getTranslationKey(), payload.getPlayer().getUsername()));
    } catch (CommandSyntaxException e) {
      AuthMod.LOGGER.catching(e);
    }
    return 1;
  }
}
