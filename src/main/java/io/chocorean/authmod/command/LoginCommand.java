package io.chocorean.authmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.util.text.ServerTranslationTextComponent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;

public class LoginCommand {

  public static void register(CommandDispatcher<CommandSource> dispatcher, Handler handler, GuardInterface guard, boolean identifierRequired) {
    LiteralArgumentBuilder<CommandSource> builder  = Commands.literal("login");
    RequiredArgumentBuilder<CommandSource, String> passwordArgs = Commands.argument("password", StringArgumentType.string())
        .executes(ctx -> execute(
          ctx.getSource(),
          handler,
          guard,
          AuthMod.toPayload(
            ctx.getSource().asPlayer(),
            identifierRequired ? StringArgumentType.getString(ctx, "id") : null,
            StringArgumentType.getString(ctx, "password")
          ))
        );
    builder.then(identifierRequired
      ? Commands.argument("id", StringArgumentType.word()).then(passwordArgs)
      : passwordArgs
    );
    dispatcher.register(builder);
  }

  /**
   * @param source
   * @param handler
   * @param guard the guard to use for login.
   * @param payload the user-provided payload.
   * @return 1 if something goes wrong, 0 otherwise.
   */
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
      source.sendFeedback(new ServerTranslationTextComponent(ExceptionToMessageMapper.getMessage(e), payload.getPlayer().getUsername()), true);
    }
    return 1;
  }

}
