package io.chocorean.authmod.core.datasource.db;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionFactoryTest {

  @Test
  void testConstructorUrl() throws Exception {
    ConnectionFactoryInterface connectionFactory = DBHelpers.initDatabase();
    Connection connection = connectionFactory.getConnection();
    assertNotNull(connection, "Connection should be configured correctly");
  }

  @Test
  void testConstructorParamsSqlite() throws Exception {
    DBHelpers.initDatabaseFile();
    ConnectionFactoryInterface connectionFactory = new ConnectionFactory(
          "sqlite",
          null,
          0,
          DBHelpers.file.getAbsolutePath(),
          null,
          null);
    Connection connection = connectionFactory.getConnection();
    assertNotNull(connection, "Connection should be configured correctly");
    assertEquals("jdbc:sqlite:" + DBHelpers.file.getAbsolutePath(), connection.getMetaData().getURL(),"JDBC URL is malformed for SQLite");
  }

  @Test
  void testConstructorParamsMariadb() {
    ConnectionFactoryInterface connectionFactory = new ConnectionFactory(
      "mariadb",
      "localhost",
      3306,
      "minecraft",
      null,
      null);
    assertEquals("jdbc:mariadb://localhost:3306/minecraft", connectionFactory.getURL(),"JDBC URL is malformed for mariadb");
  }

  @Test
  void testGetConnectionSQLError() {
    ConnectionFactoryInterface connectionFactory = new ConnectionFactory(
      "unknown-dialect",
      "localhost",
      3306,
      "minecraft",
      "awesome",
      "password");
    assertThrows(SQLException.class, connectionFactory::getConnection);
  }
}
