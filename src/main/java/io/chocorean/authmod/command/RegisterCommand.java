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
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RegisterCommand implements ICommand {
  private static final Logger LOGGER = AuthMod.LOGGER;
  private final List<String> aliases;
  private final GuardInterface guard;
  private final Handler handler;

  public RegisterCommand(Handler handler, GuardInterface guard) {
    this.handler = handler;
    aliases = new ArrayList<>();
    this.guard = guard;
    aliases.add("reg");
  }

  @Override
  public String getName() {
    return "register";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return this.handler.getMessage(this.getName() + ".usage").getUnformattedComponentText();
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
        LOGGER.info(player.getUsername() + " is registering");
        if(this.guard.register(new Payload(player, args))) {
          this.handler.authorizePlayer(entityPlayer);
          sender.sendMessage(new TextComponentString(AuthModConfig.i18n.registerSuccess));
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
