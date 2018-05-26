package com.chocorean.authmod.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AuthenticationMessageHandler implements IMessageHandler<AuthenticationPayload, IMessage> {

    @Override
    public IMessage onMessage(AuthenticationPayload message, MessageContext ctx) {
        System.out.println("received message " + message);
        return null;
    }

}
