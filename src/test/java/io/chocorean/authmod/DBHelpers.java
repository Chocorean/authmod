package io.chocorean.authmod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import io.chocorean.authmod.guard.datasource.db.ConnectionFactory;
import io.chocorean.authmod.guard.datasource.db.IConnectionFactory;

public class DBHelpers {
  public static File dataFile;

  public static void initDatabaseFile() throws IOException {
    DBHelpers.dataFile = File.createTempFile(System.getProperty("java.io.tmpdir"), "authmod.db");
    if (DBHelpers.dataFile.exists()) {
      assert DBHelpers.dataFile.delete();
    }
  }

  public static String getCreationTableQuery() {
    return "CREATE TABLE players ("
        + "id integer PRIMARY KEY,"
        + "email varchar(255) NOT NULL,"
        + "password varchar(255),"
        + "uuid varchar(255), "
        + "username varchar(255) NOT NULL,"
        + "banned INTEGER DEFAULT 0,"
        + "UNIQUE (email),"
        + "UNIQUE (uuid),"
        + "UNIQUE (username)"
        + ");";
  }

  public static void initTable(IConnectionFactory connectionFactory) throws SQLException {
    initTable("players", connectionFactory);
  }

  public static void initTable(String table, IConnectionFactory connectionFactory) throws SQLException {
    Connection connection = connectionFactory.getConnection();
    if (connection != null) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate(getCreationTableQuery().replace("players", table));
    }
    connection.close();
  }

  public static IConnectionFactory getConnectionFactory() throws IOException {
    initDatabaseFile();
    return new ConnectionFactory("jdbc:sqlite:" + dataFile.getAbsolutePath());
  }

  public static IConnectionFactory getConnectionFactory(String dialect, String host, int port, String database, String user, String password) {
    return new ConnectionFactory(dialect, host, port, database, user, password);
  }
}
