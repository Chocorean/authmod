package io.chocorean.authmod.core;

class ImARobotGuard implements GuardInterface {

  @Override
  public boolean authenticate(PayloadInterface payload) {
    return String.join(" ", payload.getArgs()).equals("I'm not a robot");
  }

  @Override
  public boolean register(PayloadInterface payload) {
    return true;
  }

}
