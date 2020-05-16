package io.chocorean.authmod.core.datasource;


import io.chocorean.authmod.core.Player;
import io.chocorean.authmod.core.datasource.db.ConnectionFactory;
import io.chocorean.authmod.core.datasource.db.ConnectionFactoryInterface;
import io.chocorean.authmod.core.datasource.db.DBHelpers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseStrategyTest {

  private ConnectionFactoryInterface connectionFactory;
  private DataSourceStrategyInterface dataSource;
  private DataSourcePlayerInterface player;

  @BeforeEach
  void init() throws Exception {
    this.connectionFactory = DBHelpers.initDatabase();
    this.dataSource = new DatabaseStrategy(this.connectionFactory);
    this.player = new DataSourcePlayer(new Player().setUsername("Whitney"));
  }

  @Test
  void testConstructor() {
    assertDoesNotThrow(() -> new DatabaseStrategy(this.connectionFactory));
  }

  @Test
  void testConstructorCannotConnect() {
    this.connectionFactory = new ConnectionFactory("jdbc:mongodb:");
    Assertions.assertThrows(SQLException.class, () -> new DatabaseStrategy(this.connectionFactory));
  }

  @Test
  void testConstructorRenameColumns() throws Exception {
    DBHelpers.initTable(this.connectionFactory, "email");
    Map<String, String> columns = new HashMap<>();
    columns.put(DatabaseStrategy.IDENTIFIER_COLUMN, "email");
    this.dataSource = new DatabaseStrategy("players", this.connectionFactory, columns, new BcryptPasswordHash());
    assertTrue(this.dataSource.add(this.player));
  }

  @Test
  void testAdd() {
    assertTrue(this.dataSource.add(this.player));
  }

  @Test
  void testAddSQLError() throws Exception {
    this.dataSource.add(this.player);
    Files.deleteIfExists(Paths.get(this.connectionFactory.getURL().split("sqlite:")[1]));
    assertNull(this.dataSource.find("test@test.com"), "The player should not exist");
  }

  @Test
  void testAddPremiumAccount() {
    this.player.setUuid("7128022b-9195-490d-9bc8-9b42ebe2a8e3");
    assertTrue(this.dataSource.add(this.player));
  }

  @Test
  void testAddDoublon() {
    assertTrue(this.dataSource.add(this.player));
    assertFalse(this.dataSource.add(this.player));
  }

  @Test
  void testFind() {
    this.dataSource.add(this.player);
    assertNotNull(this.dataSource.find(this.player.getIdentifier()), "The player should be found");
  }

  @Test
  void testFindByUsername() {
    this.dataSource.add(this.player);
    assertNotNull(this.dataSource.find(this.player.getUsername()), "The player should be found");
  }

  @Test
  void testExist() {
    this.dataSource.add(this.player);
    assertTrue(this.dataSource.exist(this.player), "The player should exist");
  }

  @Test
  void testFindNotExist() {
    this.dataSource.add(this.player);
    assertNull(this.dataSource.find("test@test.com"), "The player should not exist");
  }

  @Test
  void testFindByUsernameNotExist() {
    this.dataSource.add(this.player);
    assertNull(this.dataSource.find("Eddy le quartier"), "The player should not exist");
  }

  @Test
  void testFindNullParams() {
    this.dataSource.add(this.player);
    assertNull(this.dataSource.find(null), "It should return null");
  }

  @Test
  void testFindByUsernameNullParams() {
    this.dataSource.add(this.player);
    assertNull(this.dataSource.find(null), "It should return null");
  }

  @Test
  void testupdateSQLError() throws Exception {
    this.dataSource.add(this.player);
    Files.deleteIfExists(Paths.get(this.connectionFactory.getURL().split("sqlite:")[1]));
    assertFalse(this.dataSource.updatePassword(this.player));
  }

}
