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
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RegisterCommand implements ICommand {

    private static final Logger LOGGER = AuthMod.LOGGER;
    private final List<String> aliases;
    private final AuthModule auth;

    public RegisterCommand(IDataSourceStrategy strategy){
        aliases = new ArrayList<>();
        aliases.add("register");
        aliases.add("reg");
        this.auth = new AuthModule(strategy);
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return AuthMod.getConfig().getRegisterUsageMsg();
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayer player = (EntityPlayer) sender;
        if(Handler.isLogged(player)) {
            ((EntityPlayerMP)sender).connection.sendPacket(new SPacketChat(new TextComponentString(AuthMod.getConfig().getPlayerAlreadyLoggedMsg())));
        } else {
            if (args.length != 2) {
                ((EntityPlayerMP)sender).connection.sendPacket(new SPacketChat(new TextComponentString(AuthMod.getConfig().getWrongNumberOfArgsMsg())));
            } else {
                IPlayer playerToRegister = new Player();
                playerToRegister.setEmail(args[0]);
                playerToRegister.setPassword(args[1]);
                playerToRegister.setUsername(player.getDisplayNameString());
                try {
                    this.auth.register(playerToRegister);
                    ((EntityPlayerMP)sender).connection.sendPacket(new SPacketChat(new TextComponentString(AuthMod.getConfig().getSuccessMsg())));
                    Handler.authorizePlayer(player);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    ((EntityPlayerMP)sender).connection.sendPacket(new SPacketChat(new TextComponentString(e.getMessage())));
                }
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
