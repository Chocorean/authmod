package io.chocorean.authmod.exception;

public class WrongPasswordConfirmationException extends RegistrationException {

  public WrongPasswordConfirmationException() {
    super("Password confirmation doesn't match. Please retry");
  }
}
