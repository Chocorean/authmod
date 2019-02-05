package io.chocorean.authmod.authentication;

import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;

public class PlayerFactory {

    public static IPlayer createFromRegistrationPayload(RegistrationPayload payload) {
        IPlayer player = new Player();
        return player;
    }
}
