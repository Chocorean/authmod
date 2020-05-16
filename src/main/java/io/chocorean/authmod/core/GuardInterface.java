package io.chocorean.authmod.core;


import io.chocorean.authmod.core.exception.AuthmodError;

public interface GuardInterface {

  boolean authenticate(PayloadInterface payload) throws AuthmodError;

  boolean register(PayloadInterface payload) throws AuthmodError;
  
  boolean updatePassword(PayloadInterface newPayload) throws AuthmodError;
}
