package io.chocorean.authmod.authentification;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class PublicServerAuthenticationStrategy implements IAuthenticationStrategy {
    @Override
    public boolean login(MinecraftServer server, ICommandSender sender, String[] args) {
        return true;
    }
}
