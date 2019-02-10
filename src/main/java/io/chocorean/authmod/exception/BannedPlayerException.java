package io.chocorean.authmod.exception;

public class BannedPlayerException extends LoginException {

    public BannedPlayerException() {
        super("you've banned from this server");
    }

    public BannedPlayerException(String message) {
        super(message);
    }

}

