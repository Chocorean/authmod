package io.chocorean.authmod.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.event.Handler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class LoginWithIdentifierCommand extends LoginCommand {

  public LoginWithIdentifierCommand(Handler handler, GuardInterface guard) {
    super(handler, guard);
  }

  public RequiredArgumentBuilder<CommandSource, String> getDefaultParameters() {
    return Commands.argument("identifier", StringArgumentType.string()).then(super.getDefaultParameters());
  }

  public static int execute(CommandSource source, Handler handler, GuardInterface guard, PayloadInterface payload) {
    return LoginCommand.execute(source, handler, guard, payload);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    return execute(context.getSource(), this.handler, this.guard,
      AuthMod.toPayload(
        context.getSource().asPlayer(),
        StringArgumentType.getString(context, "identifier") ,
        StringArgumentType.getString(context, "password")));
  }

}

