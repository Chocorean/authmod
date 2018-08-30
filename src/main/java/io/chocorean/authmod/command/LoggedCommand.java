package io.chocorean.authmod.command;

import io.chocorean.authmod.event.Handler;
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

public class LoggedCommand implements ICommand {

    private static final Logger LOGGER = FMLLog.getLogger();
    private final List<String> aliases;

    public LoggedCommand(){
        this.aliases = new ArrayList<>();
        this.aliases.add("logged");
        this.aliases.add("logged?");
    }

    @Override
    public String getCommandName() {
        return "logged?";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/logged? - Tell you if you are authenticated on the server";
    }

    @Override
    public List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        EntityPlayer player = (EntityPlayer) sender;
        boolean logged = Handler.isLogged(player);
        sender.addChatMessage(new TextComponentString(logged ? "Yes" : "No"));
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