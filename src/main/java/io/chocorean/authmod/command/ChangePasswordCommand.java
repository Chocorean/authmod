package io.chocorean.authmod.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
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

public class ChangePasswordCommand implements CommandInterface {

  protected final Handler handler;
  protected final GuardInterface guard;

  public ChangePasswordCommand(Handler handler, GuardInterface guard) {
    this.handler = handler;
    this.guard = guard;
  }

  @Override
  public RequiredArgumentBuilder<CommandSource, String> getParameters() {
    return Commands.argument("old", StringArgumentType.string())
        .then(Commands.argument("new", StringArgumentType.string())
        .then(Commands.argument("confirmation", StringArgumentType.string()).executes(this)));
  }

  @Override
  public LiteralArgumentBuilder<CommandSource> getCommandBuilder() {
    return Commands.literal("changepassword").then(this.getParameters());
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    return execute(context.getSource(),
      this.handler,
      this.guard,
      AuthMod.toPayload(
        context.getSource().asPlayer(),
        StringArgumentType.getString(context, "old"),
        StringArgumentType.getString(context, "new"),
        StringArgumentType.getString(context, "confirmation")
      ));
  }

  /**
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
      source.sendFeedback(new ServerTranslationTextComponent(ExceptionToMessageMapper.getMessage(e)), false);
    }
    return 1;
  }
}
