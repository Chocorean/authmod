package io.chocorean.authmod.exception;

public class InvalidEmailException extends RegistrationException {

  public InvalidEmailException(String message) {
    super(message);
  }

  public InvalidEmailException() {
    super("");
  }
}
