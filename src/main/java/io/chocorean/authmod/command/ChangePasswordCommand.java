package io.chocorean.authmod.command;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.core.exception.ChangePasswordError;
import io.chocorean.authmod.core.i18n.ServerLanguageMap;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.util.text.ServerTranslationTextComponent;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class ChangePasswordCommand implements CommandInterface {

  protected final Handler handler;
  protected final GuardInterface guard;
  private final List<String> aliases;
  private final List<String> completions;

  public ChangePasswordCommand(Handler handler, GuardInterface guard) {
    this.handler = handler;
    this.guard = guard;
    this.aliases = new ArrayList<>();
    this.completions = new ArrayList<>();
  }

  @Override
  public String getName() {
    return "changepassword";
  }

  @Override
  public String getUsage(ICommandSender iCommandSender) {
    return ServerLanguageMap.getInstance().getOrDefault("authmod.changepassword.usage");
  }

  @Override
  public List<String> getAliases() {
    return this.aliases;
  }

  @Override
  public void execute(MinecraftServer minecraftServer, ICommandSender sender, String[] args) {
    EntityPlayer player = (EntityPlayer) sender;
    try {
      if (handler.isLogged(player)) {
        PayloadInterface payload = CommandInterface.toPayload(player, args);
        guard.updatePassword(payload);
        player.sendMessage(new ServerTranslationTextComponent("authmod.changepassword.success"));
      } else {
        player.sendMessage(new ServerTranslationTextComponent("authmod.welcome"));
      }

    } catch (ChangePasswordError e) {
      player.sendMessage(new ServerTranslationTextComponent(e.getTranslationKey()));
    } catch (AuthmodError e) {
      player.sendMessage(new ServerTranslationTextComponent(e.getTranslationKey()));
      AuthMod.LOGGER.catching(e);
    }
  }

  @Override
  public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
    return true;
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer minecraftServer, ICommandSender iCommandSender,
    String[] strings, @Nullable BlockPos blockPos) {
    return this.completions;
  }

  @Override
  public boolean isUsernameIndex(String[] strings, int i) {
    return false;
  }

  @Override
  public int compareTo(ICommand o) {
    return 0;
  }
}
