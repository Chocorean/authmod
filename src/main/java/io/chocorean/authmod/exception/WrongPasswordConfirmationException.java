package io.chocorean.authmod.exception;

public class WrongPasswordConfirmationException extends RegistrationException {
  private static final long serialVersionUID = 1L;

  public WrongPasswordConfirmationException() {
    super("Password confirmation doesn't match. Please retry");
  }
}
