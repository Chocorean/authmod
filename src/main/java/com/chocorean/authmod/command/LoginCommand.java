package com.chocorean.authmod.command;

import com.chocorean.authmod.AuthMod;
import com.chocorean.authmod.PlayerNotFoundException;
import com.chocorean.authmod.authentification.IAuthenticationStrategy;
import com.chocorean.authmod.events.Handler;
import com.chocorean.authmod.network.AuthenticationPayload;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LoginCommand implements ICommand {

    private final ArrayList aliases;

    public LoginCommand(){
        this.aliases = new ArrayList();
        this.aliases.add("login");
        this.aliases.add("log");
    }

    @Override
    public String getCommandName() {
        return "login";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/login <email> <password> - Allows you to authenticate on the server";
    }

    @Override
    public List<String> getCommandAliases() {
        return aliases;
    }



    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // checking syntax
        if (args.length != 2) {
            sender.addChatMessage(new TextComponentString("Invalid number of arguments: <email> <password>"));
        } else {
            IAuthenticationStrategy authentication = AuthMod.getAuthenticationStrategy();
            try {
                boolean isCorrect = authentication.login(args[0], args[1]);
                if(isCorrect) {
                    EntityPlayer player = (EntityPlayer) sender;
                    AuthMod.descriptors.remove(player);
                    sender.addChatMessage(new TextComponentString("Logged in successfully."));
                    ((EntityPlayerMP)sender).setPositionAndUpdate(
                            player.getPosition().getX(),
                            player.getPosition().getY(),
                            player.getPosition().getZ()
                    );
                }
                else
                    sender.addChatMessage(new TextComponentString("The password is incorrect. Try again"));
            } catch (PlayerNotFoundException e) {
                sender.addChatMessage(new TextComponentString("You are not registered on the server. Please visit TODO"));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
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
