package io.chocorean.authmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.util.text.ServerTranslationTextComponent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;

public class LoggedCommand  {

  public static void register(CommandDispatcher<CommandSource> dispatcher, Handler handler) {
    dispatcher.register(Commands.literal("logged").executes(ctx -> execute(ctx.getSource(), ctx.getSource().asPlayer(), handler)));
  }

  public static int execute(CommandSource source, PlayerEntity player, Handler handler) {
    AuthMod.LOGGER.info(String.format("%s is using /logged", player.getDisplayName().getString()));
    boolean logged = handler.isLogged(player);
    String translationKey = "logged." + (logged ? "yes" : "no");
    source.sendFeedback(new ServerTranslationTextComponent(translationKey), true);
    return 1;
  }
}
