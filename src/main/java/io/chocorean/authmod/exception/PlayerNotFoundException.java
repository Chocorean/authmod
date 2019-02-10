package io.chocorean.authmod.exception;

public class PlayerNotFoundException extends LoginException {

  public PlayerNotFoundException() {
    super("You're not registered as a player.");
  }

  public PlayerNotFoundException(String message) {
    super(message);
  }
}
