package io.chocorean.authmod.command;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.exception.LoginException;
import io.chocorean.authmod.guard.authentication.Authenticator;
import io.chocorean.authmod.guard.payload.LoginPayload;
import io.chocorean.authmod.guard.datasource.IDataSourceStrategy;
import io.chocorean.authmod.event.Handler;
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

public class LoginCommand implements ICommand {

    private static final Logger LOGGER = AuthMod.LOGGER;
    private final List<String> aliases;
    private final Authenticator authenticator;

    public LoginCommand(IDataSourceStrategy strategy){
        this.aliases = new ArrayList<>();
        this.aliases.add("login");
        this.aliases.add("log");
        this.authenticator = new Authenticator(strategy);
    }

    @Override
    public String getName() {
        return "login";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return AuthMod.getConfig().getLoginUsageMsg();
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayer player = (EntityPlayer) sender;
        LOGGER.info(player.getDisplayNameString() + " is signin in");
        if(args.length == 1 || args.length == 2) {
            if(Handler.isLogged(player)) {
                LOGGER.info("User %s tried to sign in twice.", player.getDisplayNameString());
                ((EntityPlayerMP) sender).connection.sendPacket(new SPacketChat(new TextComponentString(AuthMod.getConfig().getPlayerAlreadyLoggedMsg())));
            } else {
                LoginPayload payload = new LoginPayload();
                payload.setEmail(args.length == 2 ? args[0] : null);
                payload.setPassword(args.length == 2 ? args[1] : args[0]);
                payload.setUsername(player.getDisplayNameString());
                payload.setUuid(EntityPlayer.getUUID(player.getGameProfile()).toString());
                try {
                    boolean correct = this.authenticator.login(payload);
                    if(correct) {
                        Handler.authorizePlayer(player);
                    } else {
                        ((EntityPlayerMP)sender).connection.sendPacket(new SPacketChat(new TextComponentString("wrong")));
                    }
                } catch (LoginException e) {
                    LOGGER.error(e.getMessage());
                    ((EntityPlayerMP)sender).connection.sendPacket(new SPacketChat(new TextComponentString(e.getMessage())));
                }
            }
        } else {
            ((EntityPlayerMP)sender).connection.sendPacket(new SPacketChat(new TextComponentString(this.getUsage(sender))));
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
