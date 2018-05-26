package com.chocorean.authmod.proxy;

import com.chocorean.authmod.AuthMod;
import com.chocorean.authmod.command.LoginCommand;
import com.chocorean.authmod.network.AuthenticationMessageHandler;
import com.chocorean.authmod.network.AuthenticationPayload;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

    public void registerRenderers() {
        AuthMod.network.registerMessage(AuthenticationMessageHandler.class, AuthenticationPayload.class, 0, Side.CLIENT);
        ClientCommandHandler.instance.registerCommand(new LoginCommand());
    }

}