package io.chocorean.authmod.core.datasource;

import io.chocorean.authmod.core.PlayerInterface;

public interface DataSourcePlayerInterface extends PlayerInterface {

  boolean isBanned();

  DataSourcePlayerInterface setBanned(boolean ban);

  DataSourcePlayerInterface setPassword(String password);

  String getPassword();

  String getIdentifier();

  DataSourcePlayerInterface setIdentifier(String identifier);

}
