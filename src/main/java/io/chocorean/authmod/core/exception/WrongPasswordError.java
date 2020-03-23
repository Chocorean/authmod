package io.chocorean.authmod.core.exception;

public class WrongPasswordError extends LoginError {

  public WrongPasswordError() {
    super("Your password is incorrect. Please retry.");
  }

}
