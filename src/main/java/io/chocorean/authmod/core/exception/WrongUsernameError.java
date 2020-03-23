package io.chocorean.authmod.core.exception;

public class WrongUsernameError extends LoginError {

  public WrongUsernameError() {
    super("Your username doesn't match with the server credentials");
  }

}
