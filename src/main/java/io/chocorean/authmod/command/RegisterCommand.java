package io.chocorean.authmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.*;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.util.text.ServerTranslationTextComponent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;

public class RegisterCommand {

  public static void register(CommandDispatcher<CommandSource> dispatcher,
                              Handler handler,
                              GuardInterface guard,
                              boolean identifierRequired) {
    LiteralArgumentBuilder<CommandSource> builder = Commands.literal("register");
    RequiredArgumentBuilder<CommandSource, String> passwordArgs = Commands.argument("password", StringArgumentType.string())
      .then(
        Commands.argument("confirmation", StringArgumentType.string())
          .executes(ctx -> execute(
            ctx.getSource(),
            handler,
            guard,
            AuthMod.toPayload(
              ctx.getSource().asPlayer(),
              identifierRequired ? StringArgumentType.getString(ctx, "id") : null,
              StringArgumentType.getString(ctx, "password"),
              StringArgumentType.getString(ctx, "confirmation")))));
      builder.then(identifierRequired
        ? Commands.argument("id", StringArgumentType.word()).then(passwordArgs)
        : passwordArgs
      );
    dispatcher.register(builder);
  }

  /**
   * @param source
   * @param handler
   * @param guard The guard to use for registration.
   * @param payload the user-provided payload.
   * @return 1 if something goes wrong, 0 otherwise.
   */
  public static int execute(CommandSource source, Handler handler, GuardInterface guard, PayloadInterface payload) {

    try {
      AuthMod.LOGGER.info(String.format("%s is using /register", payload.getPlayer().getUsername()));
      PlayerEntity player = source.asPlayer();
      if (guard.register(payload)) {
        if (!handler.isLogged(source.asPlayer())) {
          handler.authorizePlayer(player);
          source.sendFeedback(new ServerTranslationTextComponent("register.success"), true);
        }
      }
      return 0;
    } catch (AuthmodError | CommandSyntaxException e) {
      source.sendFeedback(new ServerTranslationTextComponent(ExceptionToMessageMapper.getMessage(e), payload.getPlayer().getUsername()), true);
      return 1;
    }
  }
}

