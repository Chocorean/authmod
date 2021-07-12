package io.chocorean.authmod.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.event.Handler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class LoginWithIdentifierCommand extends LoginCommand implements CommandInterface {

  public LoginWithIdentifierCommand(Handler handler, GuardInterface guard) {
    super(handler, guard);
  }

  @Override
  public RequiredArgumentBuilder<CommandSource, String> getParameters() {
    return Commands.argument("identifier", StringArgumentType.string()).then(super.getParameters());
  }

  public static int execute(CommandSource source, Handler handler, GuardInterface guard, PayloadInterface payload) {
    return LoginCommand.execute(source, handler, guard, payload);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    return execute(
      context.getSource(),
      this.handler,
      this.guard,
      CommandInterface.toPayload(
        context.getSource().getPlayerOrException(),
        StringArgumentType.getString(context, "identifier"),
        StringArgumentType.getString(context, "password")
      )
    );
  }
}
