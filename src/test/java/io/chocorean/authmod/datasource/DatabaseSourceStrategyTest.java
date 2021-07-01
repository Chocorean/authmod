package io.chocorean.authmod.datasource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.chocorean.authmod.DBHelpers;
import io.chocorean.authmod.PlayerFactory;
import io.chocorean.authmod.exception.AuthmodException;
import io.chocorean.authmod.exception.PlayerAlreadyExistException;
import io.chocorean.authmod.exception.RegistrationException;
import io.chocorean.authmod.guard.datasource.DatabaseSourceStrategy;
import io.chocorean.authmod.guard.datasource.IDataSourceStrategy;
import io.chocorean.authmod.guard.datasource.db.IConnectionFactory;
import io.chocorean.authmod.model.IPlayer;

class DatabaseSourceStrategyTest {
  private IPlayer player;
  private IDataSourceStrategy dataSource;
  private IConnectionFactory connectionFactory;

  @BeforeEach
  void setupConnection() throws SQLException, IOException {
    this.connectionFactory = DBHelpers.getConnectionFactory();
    DBHelpers.initTable(this.connectionFactory);
  }

  boolean registerPlayer() throws SQLException, RegistrationException {
    this.dataSource = new DatabaseSourceStrategy(connectionFactory);
    this.player = PlayerFactory.create();
    return dataSource.add(this.player);
  }

  @Test
  void testConstructor() throws SQLException {
    new DatabaseSourceStrategy(connectionFactory);
  }

  @Test
  void testConstructorTableIsDifferent() throws SQLException {
    DBHelpers.dataFile.delete();
    Statement stmt = connectionFactory.getConnection().createStatement();
    stmt.executeUpdate(
        "CREATE TABLE players ("
            + "id integer PRIMARY KEY,"
            + "email varchar(255) NOT NULL"
            + ");");
    assertThrows(SQLException.class, () -> new DatabaseSourceStrategy(connectionFactory));
  }

  @Test
  void testAdd() throws AuthmodException, SQLException {
    boolean added = this.registerPlayer();
    assertTrue(added, "The player should be registered");
  }

  @Test
  void testAddDoublon() throws AuthmodException, SQLException {
    boolean added = this.registerPlayer();
    assertTrue(added, "The player should be registered");
    assertThrows(PlayerAlreadyExistException.class, this::registerPlayer);
  }

  @Test
  void testConstructorCustomTable() throws AuthmodException, SQLException, IOException {
    DBHelpers.dataFile.delete();
    String table = "my_table";
    this.connectionFactory = DBHelpers.getConnectionFactory();
    DBHelpers.initTable(table, this.connectionFactory);
    this.dataSource = new DatabaseSourceStrategy(table, connectionFactory);
    this.player = PlayerFactory.create();
    assertTrue(dataSource.add(this.player), "The player should be registered");
  }

  @Test
  void testAddSQLError() throws AuthmodException, SQLException {
    this.dataSource = new DatabaseSourceStrategy(connectionFactory);
    this.player = PlayerFactory.create();
    assert DBHelpers.dataFile.delete();
    boolean added = dataSource.add(this.player);
    assertFalse(added, "The player should not be registered");
  }

  @Test
  void testFindByEmail() throws RegistrationException, SQLException {
    this.registerPlayer();
    assertNotNull(dataSource.find(this.player.getEmail(), null), "The player should exist and be found");
  }

  @Test
  void testFindByEmailSQLError() throws SQLException {
    this.dataSource = new DatabaseSourceStrategy(connectionFactory);
    this.player = PlayerFactory.create();
    DBHelpers.dataFile.delete();
    assertNull(dataSource.find(this.player.getEmail(), null), "The player should not exist");
  }

  @Test
  void testFindByUsername() throws RegistrationException, SQLException {
    this.registerPlayer();
    assertNotNull(
        dataSource.find(null, player.getUsername()), "The player should exist and be found");
  }

  @Test
  void testFindNotExist() throws RegistrationException, SQLException {
    this.registerPlayer();
    assertNull(dataSource.find("test@test.fr", "test"), "The player should not exist");
  }

  @Test
  void testFindByUsernameOrEmail() throws RegistrationException, SQLException {
    this.registerPlayer();
    assertNotNull(dataSource.find(player.getEmail(), player.getUsername()),"The player should exist and be found");
  }

  @Test
  void testFindNullParams() throws AuthmodException, SQLException {
    this.registerPlayer();
    assertNull(dataSource.find(null, null), "It should return null");
  }
}
