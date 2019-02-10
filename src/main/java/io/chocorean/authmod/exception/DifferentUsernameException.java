package io.chocorean.authmod.exception;

public class DifferentUsernameException extends LoginException {

    public DifferentUsernameException() {
        super("Your username doesn't match with the server credentials");
    }

    public DifferentUsernameException(String message) {
        super(message);
    }

}

