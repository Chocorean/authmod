package io.chocorean.authmod.core;


import io.chocorean.authmod.core.exception.GuardError;

class ImARobotGuard implements GuardInterface {

  @Override
  public boolean authenticate(PayloadInterface payload) {
    try {
      return String.join(" ", payload.getArgs()).equals("I'm not a robot");
    } catch(Exception e) { }
    return false;
  }

  @Override
  public boolean register(PayloadInterface payload) {
    return true;
  }

  @Override
  public GuardError getError() {
    return null;
  }
}
