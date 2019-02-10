package io.chocorean.authmod.datasource.db;

import static org.junit.jupiter.api.Assertions.*;

import io.chocorean.authmod.DBHelpers;
import io.chocorean.authmod.guard.datasource.db.IConnectionFactory;
import io.chocorean.authmod.guard.datasource.db.IPlayersDAO;
import io.chocorean.authmod.guard.datasource.db.PlayersDAO;
import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerDAOTest {
  private IConnectionFactory connectionFactory;

  @BeforeEach
  void init() throws SQLException {
    this.connectionFactory = DBHelpers.getConnectionFactory();
    DBHelpers.initTable(this.connectionFactory);
  }

  @Test
  public void testConstructor() throws SQLException {
    new PlayersDAO(this.connectionFactory);
  }

  @Test
  public void testConstructorWrongTableStructure() {
    assertThrows(
        SQLException.class,
        () -> {
          Map<String, String> columns = new HashMap<>();
          columns.put("email", "my_email");
          new PlayersDAO(this.connectionFactory, columns);
        });
  }

  @Test
  public void testConstructorCustomColumns() throws SQLException {
    this.connectionFactory = DBHelpers.getConnectionFactory();
    String query = DBHelpers.getCreationTableQuery();
    query = query.replaceAll("username", "pseudo");
    Statement stmt = connectionFactory.getConnection().createStatement();
    stmt.executeUpdate(query);
    Map<String, String> columns = new HashMap<>();
    columns.put("username", "pseudo");
    assertDoesNotThrow(() -> new PlayersDAO(this.connectionFactory, columns));
  }

  @Test
  public void testFind() throws SQLException {
    PlayersDAO dao = new PlayersDAO(this.connectionFactory);
    IPlayer player = this.createPlayer();
    dao.create(player);
    IPlayer record = dao.find(player);
    assertEquals(player.getUsername(), record.getUsername(), "Username should not be altered");
    assertEquals(player.getEmail(), record.getEmail(), "email should not be altered");
    assertEquals(player.getUuid(), record.getUuid(), "uuid should not be altered");
    assertEquals(player.getPassword(), record.getPassword(), "password should not be altered");
    assertEquals(player.isBanned(), record.isBanned(), "banned should not be altered");
  }

  private IPlayer createPlayer() {
    IPlayer player = new Player();
    player.setUsername("batman");
    player.setEmail("bruce.wayne@wayne-enterprise.com");
    player.setPassword("gothamcity");
    player.setUuid("91da284d-b051-488c-a027-0e7452b3fdbb");
    return player;
  }

  @Test
  public void testCreate() throws SQLException {
    PlayersDAO dao = new PlayersDAO(this.connectionFactory);
    IPlayer player = this.createPlayer();
    dao.create(player);
  }

  @Test
  public void testCreateNoUUID() throws SQLException {
    PlayersDAO dao = new PlayersDAO(this.connectionFactory);
    IPlayer player = this.createPlayer();
    player.setUuid(null);
    dao.create(player);
    assertNotNull(dao.find(player));
  }

  @Test
  public void testCreateNoEmail() throws SQLException {
    PlayersDAO dao = new PlayersDAO(this.connectionFactory);
    IPlayer player = this.createPlayer();
    player.setEmail(null);
    dao.create(player);
    assertNotNull(dao.find(player));
  }
}
