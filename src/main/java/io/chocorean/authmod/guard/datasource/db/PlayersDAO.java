package io.chocorean.authmod.guard.datasource.db;

import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PlayersDAO implements IPlayersDAO<IPlayer> {

  private final String table;
  private final IConnectionFactory connectionFactory;
  private final Map<String, String> columns;
  private static final String EMAIL_COLUMN = "email";
  private static final String USERNAME_COLUMN = "username";
  private static final String UUID_COLUMN = "uuid";
  private static final String PASSWORD_COLUMN = "password";
  private static final String BANNED_COLUMN = "banned";

  public PlayersDAO(IConnectionFactory connectionFactory) throws SQLException {
    this("players", connectionFactory);
  }

  public PlayersDAO(IConnectionFactory connectionFactory, Map<String, String> columns)
      throws SQLException {
    this.table = "players";
    this.columns = columns;
    this.connectionFactory = connectionFactory;
    this.checkTable();
  }

  public PlayersDAO(String table, IConnectionFactory connectionFactory) throws SQLException {
    this.table = table;
    this.connectionFactory = connectionFactory;
    this.columns = new HashMap<>();
    this.columns.put(EMAIL_COLUMN, EMAIL_COLUMN);
    this.columns.put(BANNED_COLUMN, BANNED_COLUMN);
    this.columns.put(PASSWORD_COLUMN, PASSWORD_COLUMN);
    this.columns.put(USERNAME_COLUMN, USERNAME_COLUMN);
    this.columns.put(UUID_COLUMN, UUID_COLUMN);
    this.checkTable();
  }

  private void checkTable() throws SQLException {
    try (Connection connection = this.connectionFactory.getConnection();
        PreparedStatement stmt =
            connection.prepareStatement(
                String.format(
                    "SELECT %s,%s,%s,%s,%s FROM %s",
                    this.columns.getOrDefault(EMAIL_COLUMN, EMAIL_COLUMN),
                    this.columns.getOrDefault(BANNED_COLUMN, BANNED_COLUMN),
                    this.columns.getOrDefault(PASSWORD_COLUMN, PASSWORD_COLUMN),
                    this.columns.getOrDefault(USERNAME_COLUMN, USERNAME_COLUMN),
                    this.columns.getOrDefault(UUID_COLUMN, UUID_COLUMN),
                    this.table))) {
      stmt.executeQuery();
    }
  }

  @Override
  public void create(IPlayer player) throws SQLException {
    String query =
        String.format(
            "INSERT INTO %s(%s, %s, %s, %s) VALUES(?, ?, ?, ?)",
            this.table,
            this.columns.get(EMAIL_COLUMN),
            this.columns.get(PASSWORD_COLUMN),
            this.columns.get(USERNAME_COLUMN),
            this.columns.get(UUID_COLUMN));
    try (Connection conn = this.connectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, player.getEmail());
      stmt.setString(2, player.getPassword());
      stmt.setString(3, player.getUsername());
      if (player.getUuid() == null || player.getUuid().length() == 0)
        stmt.setNull(4, Types.VARCHAR);
      else stmt.setString(4, player.getUuid());
      stmt.executeUpdate();
    }
  }

  @Override
  public IPlayer find(IPlayer player) throws SQLException {
    try (Connection conn = this.connectionFactory.getConnection();
        PreparedStatement stmt =
            conn.prepareStatement(
                String.format(
                    "SELECT * FROM %s WHERE %s = ? OR %s = ?",
                    this.table,
                    this.columns.get(EMAIL_COLUMN),
                    this.columns.get(USERNAME_COLUMN)))) {
      stmt.setString(1, player.getEmail());
      stmt.setString(2, player.getUsername());
      ResultSet rs = stmt.executeQuery();
      return createPlayer(rs);
    }
  }

  private Player createPlayer(ResultSet rs) throws SQLException {
    Player player = null;
    if (rs != null && rs.next()) {
      player = new Player();
      player.setBanned(rs.getInt(this.columns.get(BANNED_COLUMN)) != 0);
      player.setEmail(rs.getString(this.columns.get(EMAIL_COLUMN)));
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
