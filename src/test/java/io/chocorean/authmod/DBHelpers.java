package io.chocorean.authmod;

import io.chocorean.authmod.guard.datasource.db.ConnectionFactory;
import io.chocorean.authmod.guard.datasource.db.IConnectionFactory;
import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelpers {
  public static File dataFile;

  public static void initDatabaseFile() {
    DBHelpers.dataFile = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
    if (DBHelpers.dataFile.exists()) {
      DBHelpers.dataFile.delete();
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
    Connection connection = connectionFactory.getConnection();
    if (connection != null) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate(getCreationTableQuery());
    }
  }

  public static IConnectionFactory getConnectionFactory() {
    initDatabaseFile();
    return new ConnectionFactory("jdbc:sqlite:" + dataFile.getAbsolutePath());
  }

  public static IConnectionFactory getConnectionFactory(
      String dialect, String host, int port, String database, String user, String password) {
    return new ConnectionFactory(dialect, host, port, database, user, password);
  }
}
