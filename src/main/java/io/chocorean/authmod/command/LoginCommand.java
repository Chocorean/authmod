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
      AuthMod.LOGGER.info(String.format("%s is using /login", payload.getPlayer().getUsername()));
      PlayerEntity player = source.asPlayer();
      if (!handler.isLogged(player) && guard.authenticate(payload)) {
        handler.authorizePlayer(player);
        source.sendFeedback(new ServerTranslationTextComponent("login.success"), true);
      }
      return 0;
    } catch (Exception e) {
      source.sendFeedback(new ServerTranslationTextComponent(ExceptionToMessageMapper.getMessage(e), payload.getPlayer().getUsername()), false);
    }
    return 1;
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    return execute(context.getSource(), this.handler, this.guard,
      AuthMod.toPayload(context.getSource().asPlayer(), StringArgumentType.getString(context, "password")));
  }

}

