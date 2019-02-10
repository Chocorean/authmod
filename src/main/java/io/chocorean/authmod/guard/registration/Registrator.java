package io.chocorean.authmod.guard.registration;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.exception.InvalidEmailException;
import io.chocorean.authmod.exception.PlayerAlreadyExistException;
import io.chocorean.authmod.exception.RegistrationException;
import io.chocorean.authmod.exception.WrongPasswordConfirmation;
import io.chocorean.authmod.guard.PlayerFactory;
import io.chocorean.authmod.guard.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.guard.datasource.IDataSourceStrategy;
import io.chocorean.authmod.guard.payload.RegistrationPayload;
import io.chocorean.authmod.model.IPlayer;
import java.nio.file.Paths;
import javax.validation.ConstraintViolation;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class Registrator {
  private static final Logger LOGGER = AuthMod.LOGGER;
  private final IDataSourceStrategy dataSource;

  public Registrator() {
    this(
        new FileDataSourceStrategy(
            Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile()));
  }

  public Registrator(IDataSourceStrategy dataSourceStrategy) {
    this.dataSource = dataSourceStrategy;
  }

  public boolean register(RegistrationPayload payload) throws RegistrationException {
    if (payload != null) {
      if (payload.isValid()) {
        IPlayer player = PlayerFactory.createFromRegistrationPayload(payload);
        if (this.dataSource.exist(player)) {
          throw new PlayerAlreadyExistException();
        } else {
          LOGGER.info(payload.getUsername() + " is registering");
          player.setPassword(BCrypt.hashpw(player.getPassword(), BCrypt.gensalt()));
          return this.dataSource.add(player);
        }
      } else {
        for (ConstraintViolation c : payload.getErrors()) {
          if (c.getPropertyPath().toString().equals("email")) throw new InvalidEmailException();
          if (c.getPropertyPath().toString().equals("passwordConfirmationMatches"))
            throw new WrongPasswordConfirmation();
        }
      }
    }
    return false;
  }

  public IDataSourceStrategy getDataSourceStrategy() {
    return this.dataSource;
  }
}
