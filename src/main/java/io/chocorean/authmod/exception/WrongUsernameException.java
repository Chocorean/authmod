package io.chocorean.authmod.exception;

public class WrongUsernameException extends LoginException {

  public WrongUsernameException() {
    super("Your username doesn't match with the server credentials");
  }
}
