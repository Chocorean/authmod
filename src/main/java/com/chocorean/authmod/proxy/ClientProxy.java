package com.chocorean.authmod.proxy;

import com.chocorean.authmod.commands.LoginCommand;
import net.minecraftforge.client.ClientCommandHandler;

public class ClientProxy extends CommonProxy {

    public void registerRenderers() {
        ClientCommandHandler.instance.registerCommand(new LoginCommand());
    }

}