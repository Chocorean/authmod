package io.chocorean.authmod.exception;

public class PlayerAlreadyExistException extends LoginException {

    public PlayerAlreadyExistException(String message) {
        super(message);
    }
}
