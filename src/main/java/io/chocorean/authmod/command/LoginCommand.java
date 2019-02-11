package io.chocorean.authmod.command;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.chocorean.authmod.guard.payload.IPayload;
import org.apache.logging.log4j.Logger;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.exception.*;
import io.chocorean.authmod.guard.authentication.Authenticator;
import io.chocorean.authmod.guard.datasource.IDataSourceStrategy;
import io.chocorean.authmod.guard.payload.LoginPayload;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class LoginCommand implements ICommand {
  private static final Logger LOGGER = AuthMod.LOGGER;
  private final List<String> aliases;
  private final Authenticator authenticator;
  private final Handler handler;
  private final boolean emailRequired;

  public LoginCommand(Handler handler, IDataSourceStrategy strategy) {
    this(handler, strategy, false);
  }

  public LoginCommand(Handler handler, IDataSourceStrategy strategy, boolean emailRequired) {
    this.handler = handler;
    this.aliases = new ArrayList<>();
    this.aliases.add("log");
    this.authenticator = new Authenticator(strategy);
    this.emailRequired = emailRequired;
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
    EntityPlayer player = (EntityPlayer) sender;
    LOGGER.info(player.getDisplayNameString() + " is going to log in");
    if (args.length == (this.emailRequired ? 2 : 1)) {
      if (!this.handler.isLogged(player)) {
        LoginPayload payload = createPayload(this.emailRequired, player, args);
        try {
          this.authenticator.login(payload);
          this.handler.authorizePlayer(player);
          LOGGER.info(player.getDisplayNameString() + " authenticated");
          sender.sendMessage(this.handler.getMessage(this.getName() + ".success"));
        } catch (WrongUsernameException e) {
          sender.sendMessage(this.handler.getMessage(this.getName() + ".wrongUsername"));
        } catch (WrongPasswordException e) {
          sender.sendMessage(this.handler.getMessage(this.getName() + ".wrongPassword"));
        } catch (BannedPlayerException e) {
          sender.sendMessage(this.handler.getMessage(this.getName() + ".banned"));
        } catch (PlayerNotFoundException e) {
          sender.sendMessage(this.handler.getMessage(this.getName() + ".unknown", payload.getUsername()));
        } catch (LoginException e) {
          sender.sendMessage(this.handler.getMessage("error"));
          LOGGER.error(e.getMessage());
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(this.getUsage(sender)));
    }
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }

  static LoginPayload createPayload( boolean emailRequired, EntityPlayer player, String[] args) {
    LoginPayload payload = new LoginPayload();
    payload.setEmailRequired(emailRequired);
    payload.setEmail(emailRequired ? args[0] : null);
    payload.setPassword(emailRequired ? args[1] : args[0]);
    payload.setUsername(player.getDisplayNameString());
    return payload.setUuid(EntityPlayer.getUUID(player.getGameProfile()).toString());
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
