package io.chocorean.authmod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.core.exception.LoginError;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.util.text.ServerTranslationTextComponent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;

public class LoginCommand implements CommandInterface, Command<CommandSource> {

  protected final Handler handler;
  protected final GuardInterface guard;

  public LoginCommand(Handler handler, GuardInterface guard) {
    this.handler = handler;
    this.guard = guard;
  }

  public LiteralArgumentBuilder<CommandSource> getCommandBuilder() {
    return Commands.literal("login").then(this.getParameters());
  }

  public RequiredArgumentBuilder<CommandSource, String> getParameters() {
    return Commands.argument("password", StringArgumentType.string()).executes(this);
  }

  public static int execute(CommandSource source, Handler handler, GuardInterface guard, PayloadInterface payload) {
    try {
      PlayerEntity player = source.getPlayerOrException();
      if (!handler.isLogged(player) && guard.authenticate(payload)) {
        handler.authorizePlayer(player);
        source.sendSuccess(new ServerTranslationTextComponent("authmod.login.success"), true);
      }
      return 0;
    } catch (LoginError e) {
      source.sendFailure(new ServerTranslationTextComponent(e.getTranslationKey(), payload.getPlayer().getUsername()));
    } catch (AuthmodError e) {
      source.sendFailure(new ServerTranslationTextComponent(e.getTranslationKey(), payload.getPlayer().getUsername()));
      AuthMod.LOGGER.catching(e);
    } catch (CommandSyntaxException e) {
      AuthMod.LOGGER.catching(e);
    }
    return 1;
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    return execute(
      context.getSource(),
      this.handler,
      this.guard,
      CommandInterface.toPayload(context.getSource().getPlayerOrException(), StringArgumentType.getString(context, "password"))
    );
  }
}
