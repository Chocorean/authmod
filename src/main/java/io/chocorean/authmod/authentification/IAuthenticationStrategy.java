package io.chocorean.authmod.authentification;

import io.chocorean.authmod.model.IPlayer;

public interface IAuthenticationStrategy {

    IPlayer login(IPlayer player) throws Exception;

    IPlayer register(IPlayer player) throws Exception;
}
