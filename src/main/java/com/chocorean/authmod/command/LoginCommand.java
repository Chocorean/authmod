package com.chocorean.authmod.command;

import com.chocorean.authmod.Handler;
import com.chocorean.authmod.PlayerDescriptor;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginCommand implements ICommand {
    private final ArrayList aliases;

    public LoginCommand(){
        aliases = new ArrayList();
        aliases.add("login");
        aliases.add("log");
    }

    @Override
    public String getCommandName() {
        return "login";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/login <password> - Allows you to play.";
    }

    @Override
    public List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // checking syntax
        if (args.length!=1) {
            sender.addChatMessage(new TextComponentString("Invalid number of arguments."));
            return;
        }
        String hash = RegisterCommand.generateHash(sender.getName());
        String pwd = RegisterCommand.generateHash(args[0]);
        try {
            BufferedReader br = new BufferedReader(new FileReader("mods/AuthMod/data"));
            String line=br.readLine();

            while (line != null) {
                if (line.contains(hash)) {
                    if (line.contains(pwd)){
                        // Free player here
                        for (PlayerDescriptor dc : Handler.desc) {
                            if (dc.getPlayer().getName().equals(sender.getName())){
                                Handler.desc.remove(dc);
                                sender.addChatMessage(new TextComponentString("Logged in successfully."));
                                ((EntityPlayerMP)sender).setPositionAndUpdate(dc.getPos().getX(),dc.getPos().getY(),dc.getPos().getZ());
                                return;
                            }
                        }
                    }
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sender.addChatMessage(new TextComponentString("Wrong password. Try again."));
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
