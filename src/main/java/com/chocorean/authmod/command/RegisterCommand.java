package com.chocorean.authmod.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterCommand implements ICommand {
    private final ArrayList aliases;

    public RegisterCommand(){
        aliases = new ArrayList();
        aliases.add("register");
        aliases.add("reg");
    }

    @Override
    public String getCommandName() {
        return "register";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/register <password> <password>  - Be careful when choosing it, you'll be asked to login each time you play..";
    }

    @Override
    public List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length!=2) {
            sender.addChatMessage(new TextComponentString("Invalid number of arguments."));
            return;
        }
        if (args[0].equals(args[1])){
            FileWriter writer;
            try {
                writer = new FileWriter("mods/AuthMod/data");
                writer.write(sender.getName()+" "+ args[0].hashCode());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Free player here
        } else {
            sender.addChatMessage(new TextComponentString("Passwords don't match."));
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return new ArrayList<String>();
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
