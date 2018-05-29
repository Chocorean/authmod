package io.chocorean.authmod.authentification;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.authentification.db.ConnectionFactory;
import io.chocorean.authmod.authentification.db.IPlayersDAO;
import io.chocorean.authmod.authentification.db.PlayersDAO;
import io.chocorean.authmod.exception.BanException;
import io.chocorean.authmod.exception.LoginException;
import io.chocorean.authmod.exception.PlayerNotFoundException;
import io.chocorean.authmod.exception.WrongPasswordException;
import io.chocorean.authmod.model.IPlayer;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class DatabaseAuthenticationStrategy implements IAuthenticationStrategy {

    private final IPlayersDAO playersDAO;
    private static final Logger LOGGER = FMLLog.getLogger();

    public DatabaseAuthenticationStrategy() {
        this.playersDAO = new PlayersDAO(ConnectionFactory.getConnection());
    }

    @Override
    public IPlayer login(IPlayer player) throws Exception {
        IPlayer p;
        try {
            p = this.playersDAO.findByEmailOrUsername(player.getEmail());
        } catch(SQLException e) {
            LOGGER.catching(Level.ERROR, e);
            throw new LoginException("Authentication is unavailable for the moment. Please contact " + AuthMod.config.getContact());
        }
        if(p == null)
            throw new PlayerNotFoundException(String.format("%s doesn't exist", player.getEmail()));
        if(p.isBan())
            throw new BanException(String.format("Your account is  banned (%s), please contact %s.", player.getEmail(), AuthMod.config.getContact()));
        boolean correctPassword =  BCrypt.checkpw(player.getPassword(), p.getPassword());
        if(!correctPassword)
            throw new WrongPasswordException("Wrong password, please retry");
        return p;
    }

    @Override
    public IPlayer register(IPlayer player) throws Exception {
        player.setPassword(BCrypt.hashpw(player.getPassword(), BCrypt.gensalt()));
        this.playersDAO.create(player);
        return this.playersDAO.findByEmailOrUsername(player.getEmail());
    }

}
