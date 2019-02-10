package io.chocorean.authmod.exception;

public class InvalidEmailException extends AuthmodException {

  public InvalidEmailException(String message) {
    super(message);
  }

  public InvalidEmailException() {
    super("");
  }
}
