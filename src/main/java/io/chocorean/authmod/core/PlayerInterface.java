package io.chocorean.authmod.core;

public interface PlayerInterface {

  PlayerInterface setUsername(String username);

  String getUsername();

  boolean isPremium();

  PlayerInterface setUuid(String uuid);

  String getUuid();

}
