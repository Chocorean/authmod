package io.chocorean.authmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.Payload;
import io.chocorean.authmod.core.Player;
import io.chocorean.authmod.core.PlayerInterface;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.event.Handler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.cli.Commandline;

import java.util.Collection;

public class RegisterCommand  {


  public static void register(CommandDispatcher<CommandSource> dispatcher,
                              Handler handler,
                              GuardInterface guard,
                              boolean identifierRequired) {
    LiteralArgumentBuilder<CommandSource> builder  = Commands.literal("register");
    if(identifierRequired) {
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
             identifierRequired ? StringArgumentType.getString(ctx, "id") : null,
             StringArgumentType.getString(ctx, "password"),
             StringArgumentType.getString(ctx, "confirmation"))
           )
       )
    );
    dispatcher.register(builder);
  }



  public static int execute(CommandSource source, Handler handler, GuardInterface guard,
                            String identifier, String password, String passwordConfirmation) {
    try {
      PlayerEntity player = source.asPlayer();
      PlayerInterface playerData = new Player(
        source.asPlayer().getDisplayName().toString(),
        player.getGameProfile().getId().toString()
      );
      AuthMod.LOGGER.info(playerData.getUsername() + " is registering");
      if (guard.register(new Payload(playerData, new String[]{identifier, password, passwordConfirmation}))) {
        if (!handler.isLogged(player)){
          handler.authorizePlayer(player);
          source.sendFeedback(new TranslationTextComponent("ok"), true);
        }
      }
    } catch(AuthmodError e) {
      source.sendFeedback(new StringTextComponent(ExceptionToMessageMapper.getMessage(e)), true);
    }
    catch(CommandSyntaxException e) {
      source.sendFeedback(new StringTextComponent(ExceptionToMessageMapper.getMessage(e)), true);
    }
    return 1;
  }

}
