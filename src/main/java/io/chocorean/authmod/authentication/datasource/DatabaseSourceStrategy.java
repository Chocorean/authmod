package io.chocorean.authmod.authentication.datasource;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.authentication.db.ConnectionFactory;
import io.chocorean.authmod.authentication.db.IPlayersDAO;
import io.chocorean.authmod.authentication.db.PlayersDAO;
import io.chocorean.authmod.config.AuthModDatabaseConfig;
import io.chocorean.authmod.exception.AuthmodException;
import io.chocorean.authmod.exception.LoginException;
import io.chocorean.authmod.exception.PlayerAlreadyExistException;
import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class DatabaseSourceStrategy implements IDataSourceStrategy {

    private final IPlayersDAO<IPlayer> playersDAO;
    private static final Logger LOGGER = FMLLog.log;

    public DatabaseSourceStrategy(AuthModDatabaseConfig config) {
        this.playersDAO = new PlayersDAO<Player>(ConnectionFactory.getConnection(), config.getTable());
    }

    @Override
    public IPlayer retrieve(IPlayer player) throws AuthmodException {
        IPlayer p;
        LOGGER.info("Checking authentication for " + player.getEmail());
        try {
            p = this.playersDAO.findFirst(player);
        } catch(SQLException e) {
            LOGGER.catching(Level.ERROR, e);
            throw new LoginException("Authentication is unavailable for the moment. Please contact " + AuthMod.getConfig().getContact());
        }
        return p;
    }

    @Override
    public IPlayer add(IPlayer player) throws AuthmodException {
        try {
            IPlayer saved = this.playersDAO.findFirst(player);
            if(saved != null) {
                String alreadyTaken = player.getUsername().equals(saved.getUsername()) ? player.getUsername() : player.getEmail();
                throw new PlayerAlreadyExistException(alreadyTaken + " already exists!");
            }
            this.playersDAO.create(player);
        } catch(SQLException e) {
            LOGGER.catching(Level.ERROR, e);
            throw new LoginException("Authentication is unavailable for the moment. Please contact " + AuthMod.getConfig().getContact());
        }
        return player;
    }

    @Override
    public boolean exist(IPlayer player) {
        try {
            return this.playersDAO.findFirst(player) != null;
        } catch (SQLException e) {
            LOGGER.catching(Level.ERROR, e);
        }
        return false;
    }

}
