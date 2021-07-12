package io.chocorean.authmod.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.util.text.ServerTranslationTextComponent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

public class LoggedCommand implements CommandInterface {

  private final Handler handler;

  public LoggedCommand(Handler handler) {
    this.handler = handler;
  }

  @Override
  public RequiredArgumentBuilder<CommandSource, String> getParameters() {
    return null;
  }

  @Override
  public LiteralArgumentBuilder<CommandSource> getCommandBuilder() {
    return Commands.literal("logged").executes(this);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    return execute(context.getSource(), context.getSource().getPlayerOrException(), this.handler);
  }

  public static int execute(CommandSource source, ServerPlayerEntity player, Handler handler) {
    boolean logged = handler.isLogged(player);
    String translationKey = "authmod.logged." + (logged ? "yes" : "no");
    source.sendSuccess(new ServerTranslationTextComponent(translationKey), false);
    return 0;
  }
}
