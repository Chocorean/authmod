package io.chocorean.authmod.exception;

public class WrongPasswordConfirmation extends RegistrationException {

  public WrongPasswordConfirmation() {
    super("Password confirmation doesn't match. Please retry");
  }
}
