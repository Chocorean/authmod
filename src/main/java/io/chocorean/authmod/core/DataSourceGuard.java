package io.chocorean.authmod.core;

import io.chocorean.authmod.core.datasource.DataSourcePlayer;
import io.chocorean.authmod.core.datasource.DataSourcePlayerInterface;
import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.exception.GuardError;

public class DataSourceGuard implements GuardInterface {

  private final DataSourceStrategyInterface datasource;
  private GuardError error;

  public DataSourceGuard(DataSourceStrategyInterface dataSourceStrategy) {
    this.datasource = dataSourceStrategy;
  }

  @Override
  public boolean authenticate(PayloadInterface payload) {
    try {
      DataSourcePlayerInterface foundPlayer = this.datasource.find(new DataSourcePlayer(payload.getPlayer()).getIdentifier());
      if(foundPlayer == null || foundPlayer.isBanned()) {
        return false;
      }
      return this.datasource.getHashPassword().check(foundPlayer.getPassword(), payload.getArgs()[0]);
    } catch(Exception e) {
      this.error = new GuardError(e.getMessage());
      return false;
    }
  }

  @Override
  public boolean register(PayloadInterface payload) {
    try {
      DataSourcePlayerInterface playerProxy = new DataSourcePlayer(payload.getPlayer());
      playerProxy.setPassword(this.datasource.getHashPassword().hash(payload.getArgs()[0]));
      return this.datasource.add(playerProxy);
    } catch(Exception e) {
      this.error = new GuardError(e.getMessage());
      return false;
    }
  }

  @Override
  public GuardError getError() {
    return this.error;
  }
}
