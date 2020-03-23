package io.chocorean.authmod.core.exception;

public class InvalidEmailError extends RegistrationError {

  public InvalidEmailError() {
    super("Your email is incorrect. Please retry");
  }

}
