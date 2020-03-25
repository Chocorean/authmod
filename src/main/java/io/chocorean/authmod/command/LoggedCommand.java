package io.chocorean.authmod.command;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.util.text.ServerTranslationTextComponent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class LoggedCommand  {

  public static void register(CommandDispatcher<CommandSource> dispatcher, Handler handler) {
    LiteralArgumentBuilder<CommandSource> builder = Commands.literal("logged")
      .executes(ctx -> execute(ctx.getSource(), handler));
    dispatcher.register(builder);
  }

  @VisibleForTesting
  public static int execute(CommandSource source , Handler handler) {
    try {
      boolean logged = handler.isLogged(source.asPlayer());
      String translationKey = "command.logged." + (logged ? "yes" : "no");
      source.sendFeedback(new ServerTranslationTextComponent(translationKey), false);
    } catch (CommandSyntaxException e) { source.sendErrorMessage(new StringTextComponent(e.getMessage())); }
    return 1;
  }

}
