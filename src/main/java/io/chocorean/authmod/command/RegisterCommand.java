package io.chocorean.authmod.command;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.core.exception.RegistrationError;
import io.chocorean.authmod.core.i18n.ServerLanguageMap;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.util.text.ServerTranslationTextComponent;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RegisterCommand implements CommandInterface {

  private final List<String> aliases = new ArrayList<>();
  protected final Handler handler;
  protected final GuardInterface guard;

  public RegisterCommand(Handler handler, GuardInterface guard) {
    this.handler = handler;
    this.guard = guard;
    aliases.add("reg");
  }

  @Override
  public String getName() {
    return "register";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return ServerLanguageMap.getInstance().getOrDefault("authmod.register.usage");
  }

  @Override
  public List<String> getAliases() {
    return aliases;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    EntityPlayer player = (EntityPlayer) sender;
    PayloadInterface payload = CommandInterface.toPayload(player, args);
    try {
      if (guard.register(payload) && !handler.isLogged(player)) {
        handler.authorizePlayer(player);
        sender.sendMessage(new ServerTranslationTextComponent("authmod.register.success"));
      }

    } catch (RegistrationError e) {
      sender.sendMessage(new ServerTranslationTextComponent(e.getTranslationKey(), payload.getPlayer().getUsername()));
    } catch (AuthmodError e) {
      sender.sendMessage(new ServerTranslationTextComponent(e.getTranslationKey(), payload.getPlayer().getUsername()));
      AuthMod.LOGGER.catching(e);
    }
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return new ArrayList<>();
  }

  @Override
  public boolean isUsernameIndex(String[] args, int index) {
    return false;
  }

  @Override
  public int compareTo(ICommand iCommand) {
    return this.getName().compareTo(iCommand.getName());
  }

}
