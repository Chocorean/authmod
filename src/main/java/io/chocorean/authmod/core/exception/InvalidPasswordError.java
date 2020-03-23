package io.chocorean.authmod.core.exception;

public class InvalidPasswordError extends RegistrationError {
  private static final long serialVersionUID = 1L;

  public InvalidPasswordError() {
    super("Password too short !");
  }
}
