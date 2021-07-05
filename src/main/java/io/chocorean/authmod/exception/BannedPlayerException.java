package io.chocorean.authmod.exception;

public class BannedPlayerException extends LoginException {
  private static final long serialVersionUID = 1L;

  public BannedPlayerException() {
    super("You're banned from this server");
  }
}
