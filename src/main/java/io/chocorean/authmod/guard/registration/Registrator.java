package io.chocorean.authmod.guard.registration;

import java.nio.file.Paths;

import javax.validation.ConstraintViolation;

import org.mindrot.jbcrypt.BCrypt;

import io.chocorean.authmod.exception.InvalidEmailException;
import io.chocorean.authmod.exception.InvalidPasswordException;
import io.chocorean.authmod.exception.PlayerAlreadyExistException;
import io.chocorean.authmod.exception.RegistrationException;
import io.chocorean.authmod.exception.WrongPasswordConfirmationException;
import io.chocorean.authmod.guard.PlayerFactory;
import io.chocorean.authmod.guard.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.guard.datasource.IDataSourceStrategy;
import io.chocorean.authmod.guard.payload.IPayload;
import io.chocorean.authmod.guard.payload.RegistrationPayload;
import io.chocorean.authmod.model.IPlayer;

public class Registrator {
  private final IDataSourceStrategy dataSource;

  public Registrator() {
    this(new FileDataSourceStrategy(Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile()));
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
          player.setPassword(BCrypt.hashpw(player.getPassword(), BCrypt.gensalt()));
          return this.dataSource.add(player);
        }
      } else {
        for (ConstraintViolation<IPayload> c : payload.getErrors()) {
          if (c.getPropertyPath().toString().equals("email")) throw new InvalidEmailException();
          if (c.getPropertyPath().toString().equals("passwordConfirmationMatches"))
            throw new WrongPasswordConfirmationException();
          if (c.getPropertyPath().toString().equals("password")) throw new InvalidPasswordException();
        }
      }
    }
    return false;
  }

  public IDataSourceStrategy getDataSourceStrategy() {
    return this.dataSource;
  }
}
