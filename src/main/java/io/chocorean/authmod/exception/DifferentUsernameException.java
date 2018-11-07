package io.chocorean.authmod.exception;

public class DifferentUsernameException extends LoginException {

    public DifferentUsernameException(String message) {
        super(message);
    }

}
