package io.chocorean.authmod.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.config.AuthModConfig;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.exception.InvalidEmailException;
import io.chocorean.authmod.exception.LoginException;
import io.chocorean.authmod.exception.RegistrationException;
import io.chocorean.authmod.guard.authentication.Authenticator;
import io.chocorean.authmod.guard.datasource.IDataSourceStrategy;
import io.chocorean.authmod.guard.payload.LoginPayload;
import io.chocorean.authmod.guard.payload.RegistrationPayload;
import io.chocorean.authmod.guard.registration.Registrator;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class ChangePasswordCommand implements ICommand {
    private static final Logger LOGGER = AuthMod.LOGGER;
    private final Handler handler;
    private final IDataSourceStrategy strategy;
    private final Authenticator authenticator;
    private final Registrator registrator;
	
	public ChangePasswordCommand(Handler handler, IDataSourceStrategy strategy) {
	    this.handler = handler;
	    this.strategy = strategy;
	    this.authenticator = new Authenticator(strategy);
	    this.registrator = new Registrator(strategy);
	}

	@Override
	public String getName() {
		return "changepassword";
	}

	@Override
	public String getUsage(ICommandSender sender) {
	    return AuthModConfig.i18n.changePasswordUsage;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
	    EntityPlayer player = (EntityPlayer) sender;
	    LOGGER.info(String.format("%s is using /changepassword", player.getDisplayNameString()));
	    if (args.length == 3) {
	    	if (this.handler.isLogged(player)) {
		    	// First check if password is correct
	    		LoginPayload loginPayload = createPayload(player, args[0]);
	    			try {
						if (this.authenticator.login(loginPayload)) {
							LOGGER.info("ChangePassword: login step ok");
					    	// If yes, modifying its entry
							LoginPayload newLoginPayload = createPayload(player, args[1]);
							RegistrationPayload registrationPayload = new RegistrationPayload(newLoginPayload, args[2]);
							LOGGER.info("ChangePassword: payloads ok");
							if (!this.strategy.remove(player.getName())) {
								LOGGER.error("ChangePassword: cannot remove player from database");
								sender.sendMessage(new TextComponentString(AuthModConfig.i18n.error));
								return;
							}
							try {
								if (!this.registrator.register(registrationPayload)) {
									LOGGER.error("ChangePassword: register returned false", registrationPayload);
							        sender.sendMessage(new TextComponentString(AuthModConfig.i18n.error));
									return;
								}
							} catch (RegistrationException e) {
								// password doesn't match
								sender.sendMessage(new TextComponentString(e.getMessage()));
								return;
							}
						}
					} catch (InvalidEmailException e) {
						// Shouldn't happen
						LOGGER.error("ChangePasswordCommand: InvalidEmailException");
						LOGGER.catching(e);
						sender.sendMessage(new TextComponentString(AuthModConfig.i18n.loginInvalidEmail));
						return;
					} catch (LoginException e) {
				        sender.sendMessage(new TextComponentString(e.getMessage()));
						return;
					}
	    	        sender.sendMessage(new TextComponentString(AuthModConfig.i18n.changePasswordSuccess));
	    	}
	    } else {
	    	sender.sendMessage(new TextComponentString(this.getUsage(sender)));
	    }
	}
	
	private LoginPayload createPayload(EntityPlayer player, String password) {
	    LoginPayload payload = new LoginPayload();
	    payload.setEmailRequired(false);
	    payload.setEmail(null);
	    payload.setPassword(password);
	    payload.setUsername(player.getDisplayNameString());
	    return payload.setUuid(EntityPlayer.getUUID(player.getGameProfile()).toString());
	}

	@Override
	public List<String> getAliases() {
		return new ArrayList<>();
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
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
