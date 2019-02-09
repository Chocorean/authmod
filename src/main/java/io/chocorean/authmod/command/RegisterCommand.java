package io.chocorean.authmod.command;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.config.ModConfig;
import io.chocorean.authmod.exception.AuthmodException;
import io.chocorean.authmod.guard.payload.RegistrationPayload;
import io.chocorean.authmod.guard.registration.Registrator;
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

public class RegisterCommand implements ICommand {

    private static final Logger LOGGER = AuthMod.LOGGER;
    private final List<String> aliases;
    private final Registrator registrator;

    public RegisterCommand(IDataSourceStrategy strategy){
        aliases = new ArrayList<>();
        aliases.add("register");
        aliases.add("reg");
        this.registrator = new Registrator(strategy);
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
        LOGGER.info(player.getDisplayNameString() + " is registering");
        if(args.length == 2 || args.length == 3) {
            if(Handler.isLogged(player)) {
                ((EntityPlayerMP) sender).connection.sendPacket(new SPacketChat(new TextComponentString(AuthMod.getConfig().getPlayerAlreadyLoggedMsg())));
            } else {
                RegistrationPayload payload = new RegistrationPayload(ModConfig.emailRequired);
                payload.setEmail(args.length == 3 ? args[0] : null);
                payload.setPassword(args.length == 3 ? args[1] : args[0]);
                payload.setPasswordConfirmation(args.length == 3 ? args[2] : args[1]);
                payload.setUsername(player.getDisplayNameString());
                payload.setUuid(EntityPlayer.getUUID(player.getGameProfile()).toString());
                try {
                    this.registrator.register(payload);
                    Handler.authorizePlayer(player);
                } catch (AuthmodException e) {
                    LOGGER.error(e.getMessage());
                    ((EntityPlayerMP)sender).connection.sendPacket(new SPacketChat(new TextComponentString(e.getMessage())));
                }
            }
        }
        else {
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
