package io.chocorean.authmod.command;

import io.chocorean.authmod.authentication.IAuthenticationStrategy;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RegisterCommand implements ICommand {

    private final List<String> aliases;
    public static final Logger LOGGER = FMLLog.getLogger();
    private IAuthenticationStrategy strategy;

    public RegisterCommand(IAuthenticationStrategy strategy){
        aliases = new ArrayList<>();
        aliases.add("register");
        aliases.add("reg");
        this.strategy = strategy;
    }

    @Override
    public String getCommandName() {
        return "register";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/register email password - Be careful when choosing it, you'll be asked to login each time you play..";
    }

    @Override
    public List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayer player = (EntityPlayer) sender;
        if (args.length != 2) {
            sender.addChatMessage(new TextComponentString("Invalid number of arguments expected: <email> <password>"));
        } else {
            IPlayer playerToRegister = new Player();
            playerToRegister.setEmail(args[0]);
            playerToRegister.setPassword(args[1]);
            playerToRegister.setUsername(player.getDisplayNameString());
            try {
                this.strategy.register(playerToRegister);
                sender.addChatMessage(new TextComponentString("You are registered as " + playerToRegister.getEmail() + ". Next time, please login to play!"));
                Handler.authorizePlayer(player);
            } catch (Exception e) {
                sender.addChatMessage(new TextComponentString(e.getMessage()));
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
