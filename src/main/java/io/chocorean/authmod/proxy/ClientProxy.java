package io.chocorean.authmod.proxy;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.command.LoginCommand;
import net.minecraftforge.client.ClientCommandHandler;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderers() {
        ClientCommandHandler.instance.registerCommand(new LoginCommand(AuthMod.getDataSourceStrategy()));
    }

}