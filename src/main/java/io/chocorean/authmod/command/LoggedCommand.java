package io.chocorean.authmod.command;

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

public class LoggedCommand implements CommandInterface {

  private final List<String> aliases;
  private final Handler handler;

  public LoggedCommand(Handler handler) {
    this.handler = handler;
    this.aliases = new ArrayList<>();
    this.aliases.add("logged?");
  }

  @Override
  public String getName() {
    return "logged";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return ServerLanguageMap.getInstance().getOrDefault("authmod.logged.usage");
  }

  @Override
  public List<String> getAliases() {
    return aliases;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    boolean logged = handler.isLogged((EntityPlayer) sender);
    String translationKey = "authmod.logged." + (logged ? "yes" : "no");
    sender.sendMessage(new ServerTranslationTextComponent(translationKey));
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
