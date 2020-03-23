package io.chocorean.authmod.core.exception;

public class WrongPasswordConfirmationError extends RegistrationError {

  public WrongPasswordConfirmationError() {
    super("Password confirmation doesn't match. Please retry");
  }

}
