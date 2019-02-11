package io.chocorean.authmod;

import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;

public class PlayerFactory {

  private PlayerFactory() {}

  public static IPlayer create() {
    IPlayer player = new Player();
    player.setEmail("ronnie.james@dio.org");
    player.setUsername("Ronny");
    player.setPassword("rootroot");
    player.setUuid("6f71149a-1176-4f58-810b-261d82a05350");
    return player;
  }

}
