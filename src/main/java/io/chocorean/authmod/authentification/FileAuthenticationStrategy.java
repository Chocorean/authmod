package io.chocorean.authmod.authentification;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class FileAuthenticationStrategy implements IAuthenticationStrategy {

    @Override
    public boolean login(MinecraftServer server, ICommandSender sender, String[] args) {
        return false;
    }
}
