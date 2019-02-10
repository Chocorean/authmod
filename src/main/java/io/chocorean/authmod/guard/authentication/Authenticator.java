package io.chocorean.authmod.guard.authentication;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.exception.*;
import io.chocorean.authmod.guard.datasource.IDataSourceStrategy;
import io.chocorean.authmod.guard.payload.LoginPayload;
import io.chocorean.authmod.model.IPlayer;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class Authenticator {
  private static final Logger LOGGER = AuthMod.LOGGER;
  private final IDataSourceStrategy dataSource;

  public Authenticator(IDataSourceStrategy dataSourceStrategy) {
    this.dataSource = dataSourceStrategy;
  }

  public boolean login(LoginPayload payload) throws LoginException {
    if (payload != null && payload.isValid()) {
      IPlayer player = this.dataSource.find(payload.getEmail(), payload.getUsername());
      if (player == null) throw new PlayerNotFoundException();
      if (!player.getUsername().equals(payload.getUsername())) throw new WrongUsernameException();
      if (player.isBanned()) throw new BannedPlayerException();
      LOGGER.info(payload.getUsername() + " logs in");
      boolean correct = BCrypt.checkpw(payload.getPassword(), player.getPassword());
      if (!correct) {
        throw new WrongPasswordException();
      }
      return true;
    }
    return false;
  }

  public IDataSourceStrategy getDataSourceStrategy() {
    return this.dataSource;
  }
}
