package io.chocorean.authmod.core.datasource;

import io.chocorean.authmod.core.datasource.db.ConnectionFactoryInterface;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseStrategy implements DataSourceStrategyInterface {

  private final String table;
  private final Map<String, String> columns;
  public static final String IDENTIFIER_COLUMN = "identifier";
  public static final String USERNAME_COLUMN = "username";
  public static final String UUID_COLUMN = "uuid";
  public static final String PASSWORD_COLUMN = "password";
  public static final String BANNED_COLUMN = "banned";
  private final PasswordHashInterface passwordHash;
  private final ConnectionFactoryInterface connectionFactory;

  public DatabaseStrategy(String table, ConnectionFactoryInterface connectionFactory, Map<String, String> columns, PasswordHashInterface passwordHash) throws SQLException {
    this.table = table;
    this.connectionFactory = connectionFactory;
    this.columns = new HashMap<>();
    this.columns.put(IDENTIFIER_COLUMN, IDENTIFIER_COLUMN);
    this.columns.put(BANNED_COLUMN, BANNED_COLUMN);
    this.columns.put(PASSWORD_COLUMN, PASSWORD_COLUMN);
    this.columns.put(USERNAME_COLUMN, USERNAME_COLUMN);
    this.columns.put(UUID_COLUMN, UUID_COLUMN);
    for(Map.Entry<String,String> entry: columns.entrySet()) {
      this.columns.put(entry.getKey(), entry.getValue());
    }
    this.passwordHash = passwordHash;
    this.checkTable();
  }

  public DatabaseStrategy(ConnectionFactoryInterface connectionFactory) throws SQLException {
    this("players", connectionFactory, new HashMap<>(), new BcryptPasswordHash());
  }

  @Override
  public DataSourcePlayerInterface find(String identifier) {
    try (Connection conn = this.connectionFactory.getConnection();
      PreparedStatement stmt = conn.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ?", this.table, this.columns.get(IDENTIFIER_COLUMN)))) {
      stmt.setString(1, identifier);
      ResultSet rs = stmt.executeQuery();
      return this.createPlayer(rs);
    } catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public boolean add(DataSourcePlayerInterface player) {
    String query =
      String.format(
        "INSERT INTO %s(%s, %s, %s, %s) VALUES(?, ?, ?, ?)",
        this.table,
        this.columns.get(IDENTIFIER_COLUMN),
        this.columns.get(PASSWORD_COLUMN),
        this.columns.get(USERNAME_COLUMN),
        this.columns.get(UUID_COLUMN));
    try (Connection conn = this.connectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, player.getIdentifier());
      stmt.setString(2, player.getPassword());
      stmt.setString(3, player.getUsername());
      if (player.getUuid() == null || player.getUuid().length() == 0)
        stmt.setNull(4, Types.VARCHAR);
      else stmt.setString(4, player.getUuid());
      return stmt.executeUpdate() == 1;
    } catch(Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean exist(DataSourcePlayerInterface player) {
    return this.find(player.getIdentifier()) != null;
  }

  @Override
  public PasswordHashInterface getHashPassword() {
    return this.passwordHash;
  }

  private void checkTable() throws SQLException {
    try(Connection connection = this.connectionFactory.getConnection();
        PreparedStatement stmt =
          connection.prepareStatement(
            String.format(
              "SELECT %s,%s,%s,%s,%s FROM %s",
              this.columns.getOrDefault(IDENTIFIER_COLUMN, IDENTIFIER_COLUMN),
              this.columns.getOrDefault(BANNED_COLUMN, BANNED_COLUMN),
              this.columns.getOrDefault(PASSWORD_COLUMN, PASSWORD_COLUMN),
              this.columns.getOrDefault(USERNAME_COLUMN, USERNAME_COLUMN),
              this.columns.getOrDefault(UUID_COLUMN, UUID_COLUMN),
              this.table))) {
      stmt.executeQuery();
    }
  }

  private DataSourcePlayerInterface createPlayer(ResultSet rs) throws SQLException {
    DataSourcePlayerInterface player = null;
    if (rs != null && rs.next()) {
      player = new DataSourcePlayer();
      player.setBanned(rs.getInt(this.columns.get(BANNED_COLUMN)) != 0);
      player.setIdentifier(rs.getString(this.columns.get(IDENTIFIER_COLUMN)));
      player.setPassword(rs.getString(this.columns.get(PASSWORD_COLUMN)));
      player.setUsername(rs.getString(this.columns.get(USERNAME_COLUMN)));
      player.setUuid(rs.getString(this.columns.get(UUID_COLUMN)));
    }
    if (rs != null) {
      rs.close();
    }
    return player;
  }

}

