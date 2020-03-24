package io.chocorean.authmod.core.datasource;

public interface DataSourceStrategyInterface {

  DataSourcePlayerInterface find(String identifier);

  boolean add(DataSourcePlayerInterface player);

  boolean exist(DataSourcePlayerInterface player);

  PasswordHashInterface getHashPassword();

}
