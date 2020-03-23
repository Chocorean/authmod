package io.chocorean.authmod.core.exception;

public class PlayerAlreadyExistError extends RegistrationError {

  public PlayerAlreadyExistError() {
    super("This player is already registered");
  }

  PlayerAlreadyExistError(String message) {
    super(message);
  }
}
