package io.chocorean.authmod.command;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.config.AuthModConfig;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.Payload;
import io.chocorean.authmod.core.Player;
import io.chocorean.authmod.core.PlayerInterface;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.event.Handler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LoginCommand implements ICommand {
  private static final Logger LOGGER = AuthMod.LOGGER;
  private final List<String> aliases;
  private final Handler handler;
  private final GuardInterface guard;

  public LoginCommand(Handler handler, GuardInterface guard) {
    this.handler = handler;
    this.aliases = new ArrayList<>();
    this.aliases.add("log");
    this.guard = guard;
  }

  @Override
  public String getName() {
    return "login";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return new TextComponentTranslation(this.getName() + ".usage").getUnformattedComponentText();
  }

  @Override
  public List<String> getAliases() {
    return aliases;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    EntityPlayer entityPlayer = (EntityPlayer) sender;
    try {
      if (!this.handler.isLogged(entityPlayer)) {
        PlayerInterface player = new Player(
          entityPlayer.getDisplayNameString(),
          EntityPlayer.getUUID(entityPlayer.getGameProfile()).toString()
        );
        LOGGER.info(player.getUsername() + " is authenticating");
        if(this.guard.authenticate(new Payload(player, args))) {
          this.handler.authorizePlayer(entityPlayer);
          sender.sendMessage(new TextComponentString(AuthModConfig.i18n.loginSuccess));
        }
      }
    } catch(AuthmodError e) {
      sender.sendMessage(new TextComponentString(ExceptionToMessageMapper.getMessage(e)));
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
    return true;
  }

  @Override
  public int compareTo(ICommand iCommand) {
    return this.getName().compareTo(iCommand.getName());
  }
}
