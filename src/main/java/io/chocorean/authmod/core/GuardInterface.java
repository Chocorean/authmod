package io.chocorean.authmod.core;

import io.chocorean.authmod.core.exception.GuardError;

public interface GuardInterface {

  boolean authenticate(PayloadInterface payload) throws GuardError;

  boolean register(PayloadInterface payload) throws GuardError;

}
