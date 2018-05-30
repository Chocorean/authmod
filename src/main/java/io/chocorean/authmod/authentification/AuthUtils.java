package io.chocorean.authmod.authentification;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.exception.BanException;
import io.chocorean.authmod.exception.LoginException;
import io.chocorean.authmod.exception.PlayerNotFoundException;
import io.chocorean.authmod.exception.WrongPasswordException;
import io.chocorean.authmod.model.IPlayer;
import org.mindrot.jbcrypt.BCrypt;

public class AuthUtils {

    public static IPlayer verifyAuthentication(IPlayer saved, IPlayer actual) throws LoginException {
        if(actual == null)
            throw new PlayerNotFoundException(String.format("%s doesn't exist", actual.getEmail()));
        if(actual.isBan())
            throw new BanException(String.format("Your account is  banned (%s), please contact %s.", actual.getEmail(), AuthMod.getConfig().getContact()));
        boolean correctPassword =  BCrypt.checkpw(actual.getPassword(), saved.getPassword());
        if(!correctPassword) {
            throw new WrongPasswordException("Wrong password, please retry");
        }
        return actual;
    }

    public static IPlayer Register(IPlayer player) {
        player.setPassword(BCrypt.hashpw(player.getPassword(), BCrypt.gensalt()));
        return player;
    }
}
