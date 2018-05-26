package com.chocorean.authmod.proxy;

import com.chocorean.authmod.AuthMod;
import com.chocorean.authmod.network.AuthenticationMessageHandler;
import com.chocorean.authmod.network.AuthenticationPayload;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

    public void registerRenderers() {
        AuthMod.network.registerMessage(AuthenticationMessageHandler.class, AuthenticationPayload.class, 0, Side.SERVER);
    }
}