package io.chocorean.authmod.guard.datasource.db;

import io.chocorean.authmod.AuthMod;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;

public class ConnectionFactory implements IConnectionFactory {
  private static final Logger LOGGER = AuthMod.LOGGER;
  private String url;
  private String user;
  private String password;

  public ConnectionFactory(String url) {
    this(url, null, null);
  }

  public ConnectionFactory(String url, String user, String password) {
    this.url = url;
    this.user = user;
    this.password = password;
  }

  public ConnectionFactory(
      String dialect, String host, int port, String database, String user, String password) {
    StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append("jdbc:");
    urlBuilder.append(dialect);
    if (host != null) {
      urlBuilder.append("://" + host);
      urlBuilder.append(":" + port);
      urlBuilder.append("/" + database);
    } else {
      urlBuilder.append(":" + database);
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
