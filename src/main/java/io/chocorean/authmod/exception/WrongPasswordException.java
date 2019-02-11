package io.chocorean.authmod.exception;

public class WrongPasswordException extends LoginException {

  public WrongPasswordException() {
    super("Your password is incorrect. Please retry.");
  }
}
