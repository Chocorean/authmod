package io.chocorean.authmod.exception;

public class WrongPasswordException extends LoginException {

    public WrongPasswordException(String message) {
        super(message);
    }

}

