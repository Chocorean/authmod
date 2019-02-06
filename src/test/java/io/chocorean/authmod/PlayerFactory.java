package io.chocorean.authmod;

import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;

public class PlayerFactory {

    public static IPlayer create() {
        IPlayer player = new Player();
        player.setEmail("ronnie.james@dio.org");
        player.setUsername("Ronny");
        player.setPassword("rootroot");
        return player;
    }


    public static IPlayer create(String email, String username) {
        IPlayer player = new Player();
        player.setUsername(email);
        player.setUsername(username);
        player.setPassword(email + username);
        return player;
    }

}
