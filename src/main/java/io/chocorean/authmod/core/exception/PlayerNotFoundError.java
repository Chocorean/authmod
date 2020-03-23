package io.chocorean.authmod.core.exception;

public class PlayerNotFoundError extends LoginError {
  private static final long serialVersionUID = 1L;

  public PlayerNotFoundError() {
    super("You're not registered as a player.");
  }

  public PlayerNotFoundError(String message) {
    super(message);
  }
}
