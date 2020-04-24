package io.chocorean.authmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.*;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.core.exception.PlayerAlreadyExistError;
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
    if (identifierRequired) {
      builder.then(Commands.argument("id", StringArgumentType.word()));
    }
    builder.then(
      Commands.argument("password", StringArgumentType.string())
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
                StringArgumentType.getString(ctx, "confirmation"))))
        )
    );
    dispatcher.register(builder);
  }

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
    } catch (PlayerAlreadyExistError e) {
      source.sendFeedback(new ServerTranslationTextComponent(ExceptionToMessageMapper.getMessage(e)), true);
    } catch (AuthmodError | CommandSyntaxException e) {
      AuthMod.LOGGER.catching(e);
      source.sendFeedback(new ServerTranslationTextComponent(ExceptionToMessageMapper.getMessage(e)), true);
    }
    return 1;
  }
}

