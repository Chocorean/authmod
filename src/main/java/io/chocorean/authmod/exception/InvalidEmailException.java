package io.chocorean.authmod.exception;

public class InvalidEmailException extends RegistrationException {
  private static final long serialVersionUID = 1L;

  public InvalidEmailException() {
    super("Your email is incorrect. Please retry");
  }
}
