package io.chocorean.authmod.core;

import io.chocorean.authmod.core.exception.AuthmodError;

class ImARobotGuard implements GuardInterface {

  @Override
  public boolean authenticate(PayloadInterface payload) {
    return String.join(" ", payload.getArgs()).equals("I'm not a robot");
  }

  @Override
  public boolean register(PayloadInterface payload) {
    return true;
  }

  @Override
  public boolean update(PayloadInterface oldPayload, PayloadInterface newPayload) throws AuthmodError {
    return true;
  }

}
