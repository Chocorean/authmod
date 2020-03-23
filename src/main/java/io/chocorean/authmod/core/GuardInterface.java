package io.chocorean.authmod.core;

public interface GuardInterface extends Error {

  public boolean authenticate(PayloadInterface payload);

  public boolean register(PayloadInterface payload);

}
