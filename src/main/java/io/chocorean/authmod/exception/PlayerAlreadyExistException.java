package io.chocorean.authmod.exception;

public class PlayerAlreadyExistException extends RegistrationException {

    public PlayerAlreadyExistException() {
        super("You are already registered");
    }

    public PlayerAlreadyExistException(String message) {
        super(message);
    }

}

