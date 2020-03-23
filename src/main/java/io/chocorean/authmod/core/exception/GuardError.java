package io.chocorean.authmod.core.exception;

import io.chocorean.authmod.core.Error;

public class GuardError extends Exception {

  public GuardError(String message) {
    super(message);
  }

  public GuardError() {
    super();
  }
}
