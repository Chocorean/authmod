package io.chocorean.authmod.command;

import io.chocorean.authmod.authentication.IAuthenticationStrategy;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.exception.BanException;
import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LoginCommand implements ICommand {

    private static final Logger LOGGER = FMLLog.getLogger();
    private final List<String> aliases;
    private IAuthenticationStrategy strategy;

    public LoginCommand(IAuthenticationStrategy strategy){
        this.aliases = new ArrayList<>();
        this.aliases.add("login");
        this.aliases.add("log");
        this.strategy = strategy;
    }

    @Override
    public String getCommandName() {
        return "login";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/login <email> password - Allows you to authenticate on the server";
    }

    @Override
    public List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayer player = (EntityPlayer) sender;
        IPlayer loggedPlayer = new Player();
        if(Handler.isLogged(player)) {
            sender.addChatMessage(new TextComponentString("You are already logged"));
        } else {
            loggedPlayer.setUsername(player.getDisplayNameString());
            if(args.length == 1) {
                loggedPlayer.setPassword(args[0]);
            } else {
                if(args.length == 2) {
                    loggedPlayer.setEmail(args[0]);
                    loggedPlayer.setPassword(args[1]);
                }
                else {
                    sender.addChatMessage(new TextComponentString("You have at least provide a password when using /login"));
                }
            }
            try {
                loggedPlayer = this.strategy.login(loggedPlayer);
            } catch (BanException e) {
                ((EntityPlayerMP) sender).connection.kickPlayerFromServer(e.getMessage());
            } catch (Exception e) {
                sender.addChatMessage(new TextComponentString(e.getMessage()));
                loggedPlayer = null;
            }
            if (loggedPlayer != null) {
                LOGGER.info(sender.getName() + " authenticated");
                Handler.authorizePlayer(player);
                sender.addChatMessage(new TextComponentString("Logged in successfully. Have fun!"));
                ((EntityPlayerMP) sender).setPositionAndUpdate(
                        player.getPosition().getX(),
                        player.getPosition().getY(),
                        player.getPosition().getZ()
                );
            }
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return true;
    }

    @Override
    public int compareTo(ICommand iCommand) {
        return this.getCommandName().compareTo(iCommand.getCommandName());
    }

}




