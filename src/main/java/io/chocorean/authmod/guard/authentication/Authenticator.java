package io.chocorean.authmod.guard.authentication;

import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.exception.*;
import io.chocorean.authmod.guard.datasource.IDataSourceStrategy;
import io.chocorean.authmod.guard.payload.LoginPayload;
import io.chocorean.authmod.model.IPlayer;

import javax.validation.ConstraintViolation;

public class Authenticator {
  private static final Logger LOGGER = AuthMod.LOGGER;
  private final IDataSourceStrategy dataSource;

  public Authenticator(IDataSourceStrategy dataSourceStrategy) {
    this.dataSource = dataSourceStrategy;
  }

  public boolean login(LoginPayload payload) throws LoginException, InvalidEmailException {
    if (payload != null) {
      if (payload.isValid()) {
        IPlayer player = this.dataSource.find(payload.getEmail(), payload.getUsername());
        if (player == null)
          throw new PlayerNotFoundException();
        if (!player.getUsername().equals(payload.getUsername()))
          throw new WrongUsernameException();
        if (player.isBanned())
          throw new BannedPlayerException();
        boolean correct = BCrypt.checkpw(payload.getPassword(), player.getPassword());
        if (!correct)
          throw new WrongPasswordException();
        return true;
      } else {
        for (ConstraintViolation c : payload.getErrors()) {
          if (c.getPropertyPath().toString().equals("email")) throw new InvalidEmailException();
          if (c.getPropertyPath().toString().equals("password"))
            throw new WrongPasswordException();
        }
      }
    }
    return false;
  }

  IDataSourceStrategy getDataSourceStrategy() {
    return this.dataSource;
  }
}
