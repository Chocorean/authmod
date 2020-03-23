package io.chocorean.authmod.core.datasource;

import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.core.PlayerInterface;

public interface DataSourceStrategyInterface {

  DataSourcePlayerInterface find(String identifier);

  boolean add(DataSourcePlayerInterface player);

  boolean exist(DataSourcePlayerInterface player);

  PasswordHashInterface getHashPassword();

  boolean validatePayload(PayloadInterface payload);

}
