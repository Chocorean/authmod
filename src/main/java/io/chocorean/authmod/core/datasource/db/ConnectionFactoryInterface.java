package io.chocorean.authmod.core.datasource.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactoryInterface {

  /**
   * Get a connection to database
   *
   * @return Connection object
   */
  Connection getConnection() throws SQLException;

  String getURL();
}
