package io.chocorean.authmod.exception;

import io.chocorean.authmod.AuthMod;

public class InvalidEmailException extends AuthmodException {
    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException() {
        super(String.format("Your email address must belong to the domain %s, please contact %s.", AuthMod.getConfig().getHostedDomain(), AuthMod.getConfig().getContact()));
    }

}
