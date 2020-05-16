package io.chocorean.authmod.core;

import io.chocorean.authmod.core.datasource.DataSourcePlayer;
import io.chocorean.authmod.core.datasource.DataSourcePlayerInterface;
import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.exception.*;
import io.chocorean.authmod.core.validator.ChangePasswordValidator;
import io.chocorean.authmod.core.validator.LoginValidator;
import io.chocorean.authmod.core.validator.RegistrationValidator;
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
    ValidatorInterface validator = new LoginValidator(this.identifierRequired);
    validator.validate(payload);
    DataSourcePlayerInterface foundPlayer = this.datasource.find(this.getIdentifier(payload));
    if(foundPlayer == null)
      throw new PlayerNotFoundError();
    if(foundPlayer.isBanned()) {
      throw new BannedPlayerError();
    }
    String password = payload.getArgs()[payload.getArgs().length - 1];
    if(!this.datasource.getHashPassword().check(foundPlayer.getPassword(), password)) {
      throw new WrongPasswordError();
    }
    return true;
  }

  @Override
  public boolean register(PayloadInterface payload) throws AuthmodError {
    ValidatorInterface validator = new RegistrationValidator(this.identifierRequired);
    validator.validate(payload);
    DataSourcePlayerInterface playerProxy = new DataSourcePlayer(payload.getPlayer());
    if(identifierRequired)
      playerProxy.setIdentifier(payload.getArgs()[0]);
    if(this.datasource.exist(new DataSourcePlayer(payload.getPlayer())))
      throw new PlayerAlreadyExistError();
    this.hashPassword(playerProxy, payload.getArgs()[payload.getArgs().length - 1]);
    return this.datasource.add(playerProxy);
  }

  @Override
  public boolean updatePassword(PayloadInterface payload) throws AuthmodError {
    ValidatorInterface validator = new ChangePasswordValidator();
    validator.validate(payload);
    DataSourcePlayerInterface foundPlayer = this.datasource.findByUsername(payload.getPlayer().getUsername());
    if(foundPlayer == null)
      return false;
    if(!this.datasource.getHashPassword().check(foundPlayer.getPassword(), payload.getArgs()[0]))
      throw new WrongPasswordError();
    this.hashPassword(foundPlayer, payload.getArgs()[payload.getArgs().length - 1]);
    return this.datasource.updatePassword(foundPlayer);
  }

  private String getIdentifier(PayloadInterface payload) {
    return this.identifierRequired ? payload.getArgs()[0] : payload.getPlayer().getUsername();
  }

  private void hashPassword(DataSourcePlayerInterface player, String password) {
    player.setPassword(this.datasource.getHashPassword().hash(password));
  }

}
