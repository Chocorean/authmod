package io.chocorean.authmod.command;

import io.chocorean.authmod.event.Handler;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class LoggedCommand implements ICommand {
  private final List<String> aliases;
  private final Handler handler;

  public LoggedCommand(Handler handler) {
    this.handler = handler;
    this.aliases = new ArrayList<>();
    this.aliases.add("logged");
    this.aliases.add("logged?");
  }

  @Override
  public String getName() {
    return "logged?";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/logged? - Tell you if you are authenticated on the server";
  }

  @Override
  public List<String> getAliases() {
    return aliases;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    EntityPlayer player = (EntityPlayer) sender;
    boolean logged = this.handler.isLogged(player);
    ((EntityPlayerMP) sender)
        .connection.sendPacket(new SPacketChat(new TextComponentString(logged ? "Yes" : "No")));
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return new ArrayList<>();
  }

  @Override
  public boolean isUsernameIndex(String[] args, int index) {
    return true;
  }

  @Override
  public int compareTo(ICommand iCommand) {
    return this.getName().compareTo(iCommand.getName());
  }
}
