package io.chocorean.authmod.exception;

import io.chocorean.authmod.AuthMod;

public class UnauthorizedHostedDomainException extends Exception {

    public UnauthorizedHostedDomainException(String message) {
        super(message);
    }

    public UnauthorizedHostedDomainException() {
        super(String.format("Your email address must belong to the domain %s, please contact %s.", AuthMod.getConfig().getHostedDomain(), AuthMod.getConfig().getContact()));
    }

}
