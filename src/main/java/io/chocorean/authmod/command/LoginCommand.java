package io.chocorean.authmod.command;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.config.AuthModConfig;
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
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
    return AuthModConfig.i18n.loginUsage;
  }

  @Override
  public List<String> getAliases() {
    return aliases;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    EntityPlayer player = (EntityPlayer) sender;
    if (args.length == (this.emailRequired ? 2 : 1)) {
      if (!this.handler.isLogged(player)) {
        LoginPayload payload = createPayload(this.emailRequired, player, args);
        LOGGER.info(payload.toString() + " is going to log in");
        try {
          if(this.authenticator.login(payload)) {
            this.handler.authorizePlayer(player);
            LOGGER.info(player.getDisplayNameString() + " authenticated");
          }
          sender.sendMessage(new TextComponentString(AuthModConfig.i18n.loginSuccess));
        } catch (WrongUsernameException e) {
          sender.sendMessage(new TextComponentString(AuthModConfig.i18n.loginWrongUsername));
        } catch (WrongPasswordException e) {
          sender.sendMessage(new TextComponentString(AuthModConfig.i18n.loginWrongPassword));
        } catch (BannedPlayerException e) {
          sender.sendMessage(new TextComponentString(AuthModConfig.i18n.loginBanned));
        } catch (PlayerNotFoundException e) {
          sender.sendMessage(new TextComponentString(String.format(AuthModConfig.i18n.loginUnknown, player.getDisplayNameString())));
        } catch (InvalidEmailException e) {
          sender.sendMessage(new TextComponentString(AuthModConfig.i18n.loginInvalidEmail));
        } catch (LoginException e) {
          sender.sendMessage(new TextComponentString(AuthModConfig.i18n.error));
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
