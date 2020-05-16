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

public class ChangePasswordCommand  {

  public static void register(CommandDispatcher<CommandSource> dispatcher,
                              Handler handler,
                              GuardInterface guard) {
    LiteralArgumentBuilder<CommandSource> builder = Commands.literal("changepassword");
    builder.then(
      Commands.argument("old_password", StringArgumentType.string())
        .then(
          Commands.argument("new_password", StringArgumentType.string())
            .then(Commands.argument("confirmation", StringArgumentType.string())
              .executes(
                ctx -> execute(ctx.getSource(),
                  handler,
                  guard,
                  AuthMod.toPayload(
                    ctx.getSource().asPlayer(),
                    StringArgumentType.getString(ctx, "old_password"),
                    StringArgumentType.getString(ctx, "new_password"),
                    StringArgumentType.getString(ctx, "confirmation")
                  ))))));
    dispatcher.register(builder);
  }

  /**
   *
   * @param source
   * @param handler
   * @param guard
   * @param payload
   * @return 1 if something goes wrong, 0 otherwise.
   */
  public static int execute(CommandSource source, Handler handler, GuardInterface guard, PayloadInterface payload) {
    try {
      AuthMod.LOGGER.info(String.format("%s is using /changepassword", payload.getPlayer().getUsername()));
      PlayerEntity player = source.asPlayer();
      if (handler.isLogged(player)) {
        guard.updatePassword(payload);
        source.sendFeedback(new ServerTranslationTextComponent("changepassword.success"), true);
        return 0;
      }
      else {
        source.sendFeedback(new ServerTranslationTextComponent("welcome"), true);
      }
    } catch (AuthmodError | CommandSyntaxException e) {
      source.sendFeedback(new ServerTranslationTextComponent(ExceptionToMessageMapper.getMessage(e)), true);
    }
    return 1;
  }
}
