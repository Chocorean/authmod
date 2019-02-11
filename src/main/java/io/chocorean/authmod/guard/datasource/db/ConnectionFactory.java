package io.chocorean.authmod.guard.datasource.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;

import io.chocorean.authmod.AuthMod;

public class ConnectionFactory implements IConnectionFactory {
  private static final Logger LOGGER = AuthMod.LOGGER;
  private final String url;
  private final String user;
  private final String password;

  public ConnectionFactory(String url) {
    this.url = url;
    this.password = null;
    this.user = null;
  }

  public ConnectionFactory(
      String dialect, String host, int port, String database, String user, String password) {
    StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append("jdbc:").append(dialect);
    if (host != null) {
      urlBuilder.append("://").append(host).append(":").append(port).append("/").append(database);
    } else {
      urlBuilder.append(":").append(database);
    }
    this.url = urlBuilder.toString();
    this.user = user;
    this.password = password;
  }

  @Override
  public Connection getConnection() throws SQLException {
    try {
      return user == null
          ? DriverManager.getConnection(this.url)
          : DriverManager.getConnection(this.url, this.user, this.password);
    } catch (SQLException ex) {
      LOGGER.error(ex.getStackTrace());
      throw new SQLException(ex);
    }
  }

  @Override
  public String getURL() {
    return this.url;
  }
}
