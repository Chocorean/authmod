package io.chocorean.authmod.core.exception;

public class BannedPlayerError extends LoginError {

  public BannedPlayerError() {
    super("you've banned from this server");
  }

}
