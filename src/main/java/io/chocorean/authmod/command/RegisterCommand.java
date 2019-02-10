package io.chocorean.authmod.command;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.exception.AuthmodException;
import io.chocorean.authmod.guard.datasource.IDataSourceStrategy;
import io.chocorean.authmod.guard.payload.RegistrationPayload;
import io.chocorean.authmod.guard.registration.Registrator;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
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
  private final Registrator registrator;
  private final Handler handler;
  private final boolean emailRequired;

  public RegisterCommand(Handler handler, IDataSourceStrategy strategy, boolean emailRequired) {
    this.handler = handler;
    aliases = new ArrayList<>();
    aliases.add("register");
    aliases.add("reg");
    this.registrator = new Registrator(strategy);
    this.emailRequired = emailRequired;
  }

  public RegisterCommand(Handler handler, IDataSourceStrategy strategy) {
    this(handler, strategy, false);
  }

  @Override
  public String getName() {
    return "register";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "";
  }

  @Override
  public List<String> getAliases() {
    return aliases;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    try {
      EntityPlayer player = (EntityPlayer) sender;
      LOGGER.info(player.getDisplayNameString() + " is registering");
      if (this.emailRequired && args.length == 3 || !this.emailRequired && args.length == 2) {
        if (this.handler.isLogged(player)) {
          ((EntityPlayerMP) sender).connection.sendPacket(new SPacketChat(new TextComponentString("")));
        } else {
          RegistrationPayload payload = this.createPayload(player, args);
          boolean registered = this.registrator.register(payload);
          if (registered) this.handler.authorizePlayer(player);
        }
      } else {
        ((EntityPlayerMP) sender).connection.sendPacket(new SPacketChat(new TextComponentString(this.getUsage(sender))));
      }
    } catch (AuthmodException e) {
      LOGGER.error(e.getMessage());
      ((EntityPlayerMP) sender)
        .connection.sendPacket(new SPacketChat(new TextComponentString(e.getMessage())));
    }
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

  private RegistrationPayload createPayload(EntityPlayer player, String[] args) {
    RegistrationPayload payload = new RegistrationPayload();
    payload.setEmailRequired(this.emailRequired);
    payload.setEmail(this.emailRequired ? args[0] : null);
    payload.setPassword(this.emailRequired ? args[1] : args[0]);
    payload.setPasswordConfirmation(this.emailRequired ? args[2] : args[1]);
    payload.setUsername(player.getDisplayNameString());
    return payload.setUuid(EntityPlayer.getUUID(player.getGameProfile()).toString());
  }
}
