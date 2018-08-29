package io.chocorean.authmod.authentication;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.authentication.db.ConnectionFactory;
import io.chocorean.authmod.authentication.db.IPlayersDAO;
import io.chocorean.authmod.authentication.db.PlayersDAO;
import io.chocorean.authmod.config.AuthModDatabaseConfig;
import io.chocorean.authmod.exception.LoginException;
import io.chocorean.authmod.exception.PlayerAlreadyExistException;
import io.chocorean.authmod.model.IPlayer;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class DatabaseAuthenticationStrategy implements IAuthenticationStrategy {

    private final IPlayersDAO playersDAO;
    public static final Logger LOGGER = FMLLog.getLogger();

    public DatabaseAuthenticationStrategy(AuthModDatabaseConfig config) {
        this.playersDAO = new PlayersDAO(ConnectionFactory.getConnection(), config.getTable());
    }

    @Override
    public IPlayer login(IPlayer player) throws Exception {
        IPlayer p;
        String data = player.getEmail() == null ? player.getUsername() : player.getEmail();
        LOGGER.info("Checking authentication for " + player.getEmail());
        try {
            p = this.playersDAO.findByEmailOrUsername(data);
        } catch(SQLException e) {
            LOGGER.catching(Level.ERROR, e);
            throw new LoginException("Authentication is unavailable for the moment. Please contact " + AuthMod.getConfig().getContact());
        }
        p = AuthUtils.verifyAuthentication(p, player);
        return p;
    }

    @Override
    public IPlayer register(IPlayer player) throws Exception {
        player = AuthUtils.Register(player);
        try {
            if(this.playersDAO.findByEmail(player.getEmail()) != null)
                throw new PlayerAlreadyExistException(player.getEmail() + " already exists!");
            this.playersDAO.create(player);
        } catch(SQLException e) {
            LOGGER.catching(Level.ERROR, e);
            throw new LoginException("Authentication is unavailable for the moment. Please contact " + AuthMod.getConfig().getContact());
        }
        return this.playersDAO.findByEmailOrUsername(player.getEmail());
    }

}
