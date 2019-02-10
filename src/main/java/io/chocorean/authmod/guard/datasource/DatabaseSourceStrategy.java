package io.chocorean.authmod.guard.datasource;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.exception.PlayerAlreadyExistException;
import io.chocorean.authmod.exception.RegistrationException;
import io.chocorean.authmod.guard.datasource.db.IConnectionFactory;
import io.chocorean.authmod.guard.datasource.db.IPlayersDAO;
import io.chocorean.authmod.guard.datasource.db.PlayersDAO;
import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;
import java.sql.SQLException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class DatabaseSourceStrategy implements IDataSourceStrategy {
  private final IPlayersDAO<IPlayer> playersDAO;
  private static final Logger LOGGER = AuthMod.LOGGER;

  public DatabaseSourceStrategy(String table, IConnectionFactory connectionFactory)
      throws SQLException {
    this.playersDAO = new PlayersDAO(table, connectionFactory);
  }

  public DatabaseSourceStrategy(IConnectionFactory connectionFactory) throws SQLException {
    this.playersDAO = new PlayersDAO("players", connectionFactory);
  }

  @Override
  public IPlayer find(String email, String username) {
    IPlayer p;
    try {
      p = this.playersDAO.find(new Player().setEmail(email).setUsername(username));
      return p;
    } catch (SQLException e) {
      LOGGER.catching(Level.ERROR, e);
    }
    return null;
  }

  @Override
  public boolean add(IPlayer player) throws RegistrationException {
    try {
      boolean alreadyExist = this.exist(player);
      if (alreadyExist) {
        throw new PlayerAlreadyExistException();
      }
      this.playersDAO.create(player);
      return true;
    } catch (SQLException e) {
      LOGGER.catching(Level.ERROR, e);
      return false;
    }
  }

  @Override
  public boolean exist(IPlayer player) {
    try {
      return this.playersDAO.find(player) != null;
    } catch (SQLException e) {
      LOGGER.catching(Level.ERROR, e);
    }
    return false;
  }
}
