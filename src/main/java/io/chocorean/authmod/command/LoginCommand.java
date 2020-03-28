package io.chocorean.authmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.util.text.ServerTranslationTextComponent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;

public class LoginCommand {

  public static void register(CommandDispatcher<CommandSource> dispatcher, Handler handler, GuardInterface guard, boolean identifierRequired) {
    LiteralArgumentBuilder<CommandSource> builder  = Commands.literal("login");
    if(identifierRequired) {
      builder.then(Commands.argument("id", StringArgumentType.word()));
    }
    builder.then(
      Commands.argument("password", StringArgumentType.string())
        .executes(ctx -> execute(
          ctx.getSource(),
          handler,
          guard,
          AuthMod.toPayload(
            ctx.getSource().asPlayer(),
            identifierRequired ? StringArgumentType.getString(ctx, "id") : null,
            StringArgumentType.getString(ctx, "password")
            ))
        )
    );
    dispatcher.register(builder);
  }

  public static int execute(CommandSource source, Handler handler, GuardInterface guard, PayloadInterface payload) {
    try {
      PlayerEntity player = source.asPlayer();
      if (!handler.isLogged(player)) {
        AuthMod.LOGGER.info(payload.getPlayer().getUsername() + " is authenticating");
        if (guard.authenticate(payload)) {
          handler.authorizePlayer(player);
          player.sendMessage(new ServerTranslationTextComponent("login.success"));
        }
      }
    } catch (AuthmodError | CommandSyntaxException e) {
      source.sendFeedback(new ServerTranslationTextComponent(ExceptionToMessageMapper.getMessage(e)), false);
    }
    return 1;
  }

}
