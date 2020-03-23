package io.chocorean.authmod.core;

import io.chocorean.authmod.core.exception.GuardError;

public interface Error {

  public GuardError getError();
}
