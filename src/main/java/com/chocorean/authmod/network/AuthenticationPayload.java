package com.chocorean.authmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class AuthenticationPayload implements IMessage {

    private int length;
    private String user;
    private String password;

    @Override
    public void fromBytes(ByteBuf buf) {
        this.length = buf.readInt();
        String data = buf.readBytes(length).toString();
        System.out.println("###### " + data);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        String data = String.format("%s %s", this.user, this.password);
        buf.writeInt(data.length());
        buf.writeBytes(data.getBytes());
    }
}
