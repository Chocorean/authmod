package io.chocorean.authmod.command;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.authentication.AuthModule;
import io.chocorean.authmod.authentication.datasource.IDataSourceStrategy;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LoginCommand implements ICommand {

    private static final Logger LOGGER = FMLLog.log;
    private final List<String> aliases;
    private final AuthModule auth;

    public LoginCommand(IDataSourceStrategy strategy){
        this.aliases = new ArrayList<>();
        this.aliases.add("login");
        this.aliases.add("log");
        this.auth = new AuthModule(strategy);
    }

    @Override
    public String getName() {
        return "login";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/login email password - Allows you to authenticate on the server";
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayer player = (EntityPlayer) sender;
        IPlayer loggedPlayer = new Player();
        if(Handler.isLogged(player)) {
            ((EntityPlayerMP)sender).connection.sendPacket(new SPacketChat(new TextComponentString("You are already logged")));
        } else {
            if(args.length == 2) {
                loggedPlayer.setEmail(args[0]);
                loggedPlayer.setPassword(args[1]);
                loggedPlayer.setUsername(player.getDisplayNameString());
                loggedPlayer.setUuid(EntityPlayer.getUUID(player.getGameProfile()).toString());
                try {
                    loggedPlayer = this.auth.login(loggedPlayer);
                    if (loggedPlayer != null) {
                        AuthMod.LOGGER.info(sender.getName() + " authenticated");
                        Handler.authorizePlayer(player);
                        ((EntityPlayerMP)sender).connection.sendPacket(new SPacketChat(new TextComponentString("Logged in successfully. Have fun!")));
                        ((EntityPlayerMP) sender).setPositionAndUpdate(
                                player.getPosition().getX(),
                                player.getPosition().getY(),
                                player.getPosition().getZ()
                        );
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    ((EntityPlayerMP)sender).connection.sendPacket(new SPacketChat(new TextComponentString(e.getMessage())));
                }
            }
            else {
                ((EntityPlayerMP)sender).connection.sendPacket(new SPacketChat(new TextComponentString("You have at least provide the email address and the password to log in.")));
            }
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




