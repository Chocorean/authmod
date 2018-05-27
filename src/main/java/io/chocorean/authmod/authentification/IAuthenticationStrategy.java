package io.chocorean.authmod.authentification;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public interface IAuthenticationStrategy {

    boolean login(MinecraftServer server, ICommandSender sender, String[] args) throws Exception;
}
