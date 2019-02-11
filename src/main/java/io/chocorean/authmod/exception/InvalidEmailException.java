package io.chocorean.authmod.exception;

public class InvalidEmailException extends RegistrationException {

  public InvalidEmailException() {
    super("Your email is incorrect. Please retry");
  }
}
