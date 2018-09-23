package io.chocorean.authmod.authentication;

import io.chocorean.authmod.exception.AuthmodException;
import io.chocorean.authmod.model.IPlayer;

public interface IAuthenticationStrategy {

    IPlayer login(IPlayer player) throws AuthmodException;

    IPlayer register(IPlayer player) throws AuthmodException;
    
    boolean exist(IPlayer player);


}
