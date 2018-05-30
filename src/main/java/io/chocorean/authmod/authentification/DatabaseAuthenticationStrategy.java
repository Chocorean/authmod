package io.chocorean.authmod.authentification;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.authentification.db.ConnectionFactory;
import io.chocorean.authmod.authentification.db.IPlayersDAO;
import io.chocorean.authmod.authentification.db.PlayersDAO;
import io.chocorean.authmod.exception.LoginException;
import io.chocorean.authmod.exception.PlayerAlreadyExistException;
import io.chocorean.authmod.model.IPlayer;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

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
        p = AuthUtils.verifyAuthentication(p, player);
        return p;
    }

    @Override
    public IPlayer register(IPlayer player) throws Exception {
        player = AuthUtils.Register(player);
        try {
            if(this.playersDAO.findByEmail(player.getEmail()) !=null)
                throw new PlayerAlreadyExistException(player.getEmail() + " already exists!");
            this.playersDAO.create(player);
        } catch(SQLException e) {
            LOGGER.catching(Level.ERROR, e);
            throw new LoginException("Authentication is unavailable for the moment. Please contact " + AuthMod.config.getContact());
        }
        return this.playersDAO.findByEmailOrUsername(player.getEmail());
    }

}
