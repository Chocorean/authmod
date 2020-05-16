package io.chocorean.authmod.core.datasource;

public interface DataSourceStrategyInterface {

  DataSourcePlayerInterface find(String identifier);

  DataSourcePlayerInterface findByUsername(String username);

  boolean add(DataSourcePlayerInterface player);

  boolean exist(DataSourcePlayerInterface player);

  boolean updatePassword(DataSourcePlayerInterface player);

  PasswordHashInterface getHashPassword();

}
