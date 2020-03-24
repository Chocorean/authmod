package io.chocorean.authmod.core;

import io.chocorean.authmod.core.datasource.DataSourcePlayer;
import io.chocorean.authmod.core.datasource.DataSourcePlayerInterface;
import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.core.exception.BannedPlayerError;
import io.chocorean.authmod.core.exception.PlayerAlreadyExistError;
import io.chocorean.authmod.core.exception.PlayerNotFoundError;
import io.chocorean.authmod.core.validator.DataSourceLoginValidator;
import io.chocorean.authmod.core.validator.DataSourceRegistrationValidator;
import io.chocorean.authmod.core.validator.ValidatorInterface;

public class DataSourceGuard implements GuardInterface {

  private final DataSourceStrategyInterface datasource;
  private final boolean identifierRequired;

  public DataSourceGuard(DataSourceStrategyInterface dataSourceStrategy, boolean identifierRequired) {
    this.datasource = dataSourceStrategy;
    this.identifierRequired = identifierRequired;
  }

  public DataSourceGuard(DataSourceStrategyInterface dataSourceStrategy) {
    this(dataSourceStrategy, false);
  }

  @Override
  public boolean authenticate(PayloadInterface payload) throws AuthmodError {
    ValidatorInterface validator = new DataSourceLoginValidator(this.identifierRequired);
    boolean payloadValid = validator.validate(payload);
    if(payloadValid) {
      String password = payload.getArgs()[payload.getArgs().length - 1];
      DataSourcePlayerInterface foundPlayer = this.datasource.find(new DataSourcePlayer(payload.getPlayer()).getIdentifier());
      if(foundPlayer == null)
        throw new PlayerNotFoundError();
      if(foundPlayer.isBanned()) {
        throw new BannedPlayerError();
      }
      return this.datasource.getHashPassword().check(foundPlayer.getPassword(), password);
    }
    return false;
  }

  @Override
  public boolean register(PayloadInterface payload) throws AuthmodError {
    ValidatorInterface validator = new DataSourceRegistrationValidator(this.identifierRequired);
    boolean payloadValid = validator.validate(payload);
    if(payloadValid) {
      DataSourcePlayerInterface playerProxy = new DataSourcePlayer(payload.getPlayer());
      if(this.datasource.exist(new DataSourcePlayer(payload.getPlayer()))) {
        throw new PlayerAlreadyExistError();
      }
      playerProxy.setPassword(this.datasource.getHashPassword().hash(payload.getArgs()[payload.getArgs().length - 1]));
      return this.datasource.add(playerProxy);
    }
    return false;
  }

}
