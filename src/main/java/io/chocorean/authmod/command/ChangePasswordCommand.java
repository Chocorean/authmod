package io.chocorean.authmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.core.exception.SamePasswordError;
import io.chocorean.authmod.core.exception.WrongOldPasswordError;
import io.chocorean.authmod.core.exception.WrongPasswordConfirmationError;
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
                	        null,
                	        StringArgumentType.getString(ctx, "old_password")
                	    ),
                		AuthMod.toPayload(
                          ctx.getSource().asPlayer(),
                          null,
                          StringArgumentType.getString(ctx, "new_password"),
                          StringArgumentType.getString(ctx, "confirmation")
      ))))));
    dispatcher.register(builder);
  }

  public static int execute(CommandSource source, Handler handler, GuardInterface guard, PayloadInterface oldPayload, PayloadInterface newPayload) {
    try {
      AuthMod.LOGGER.info(String.format("%s is using /changepassword", oldPayload.getPlayer().getUsername()));
      PlayerEntity player = source.asPlayer();
      if (handler.isLogged(player)) {
          try {
            guard.update(oldPayload, newPayload);
          } catch (WrongOldPasswordError e) {
            source.sendFeedback(new ServerTranslationTextComponent("changepassword.failure_old"), true);
            return 1;
          } catch (WrongPasswordConfirmationError e) {
            source.sendFeedback(new ServerTranslationTextComponent("changepassword.failure_new"), true);
            return 1;
          } catch (SamePasswordError e) {
            source.sendFeedback(new ServerTranslationTextComponent("changepassword.same_password"), true);
          } catch (AuthmodError e) {
            AuthMod.LOGGER.catching(e);
            return 1;
          }
          source.sendFeedback(new ServerTranslationTextComponent("changepassword.success"), true);
          return 0;
      } else {
        source.sendFeedback(new ServerTranslationTextComponent("welcome"), true);
        return 1;
      }
    } catch (CommandSyntaxException e) {
      source.sendFeedback(new ServerTranslationTextComponent(ExceptionToMessageMapper.getMessage(e), oldPayload.getPlayer().getUsername()), false);
      return 1;
    }
  }
}
