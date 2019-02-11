package io.chocorean.authmod.exception;

public class PlayerAlreadyExistException extends RegistrationException {

  public PlayerAlreadyExistException() {
    super("This player is already registered");
  }
}
