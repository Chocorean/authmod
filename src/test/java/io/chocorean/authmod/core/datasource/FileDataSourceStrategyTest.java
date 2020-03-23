package io.chocorean.authmod.core.datasource;


import io.chocorean.authmod.core.Player;
import io.chocorean.authmod.core.PlayerInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileDataSourceStrategyTest {

  private File file;
  private DataSourcePlayerInterface player;
  private DataSourceStrategyInterface dataSource;

  @BeforeEach
  void init() {
    this.file = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
    this.dataSource = new FileDataSourceStrategy(this.file);
    if (this.file.exists()) {
      this.file.delete();
    }
    this.player = new DataSourcePlayer(new Player().setUsername("Whitney"));
  }

  @Test
  void testConstructor() {
    new FileDataSourceStrategy(this.file);
    assertTrue(this.file.exists(), "The strategy should create a CSV file automatically");
  }

  public boolean registerPlayer(PlayerInterface player) {
    DataSourcePlayerInterface playerToRegister = new DataSourcePlayer(player);
    return dataSource.add(playerToRegister);
  }

  @Test
  void testAdd() {
    assertTrue(this.registerPlayer(this.player));
  }

  @Test
  void testAddDoublon() {
    assertTrue(this.registerPlayer(this.player));
    assertFalse(this.registerPlayer(this.player));
  }

  @Test
  void testFileModified() throws Exception {
    this.registerPlayer(this.player);
    Thread.sleep(1000);
    BufferedWriter writer = new BufferedWriter(new FileWriter(this.file, true));
    writer.append("mcdostone, mcdostone, password, false");
    writer.flush();
    writer.close();
    assertNotNull(this.dataSource.find("mcdostone"));
  }

  @Test
  void testFind() {
    this.registerPlayer(this.player);
    assertNotNull(dataSource.find(this.player.getIdentifier()), "The player should be found");
  }

  @Test
  void testFindNotExist() {
    this.registerPlayer(this.player);
    assertNull(dataSource.find("test@test.com"), "The player should not exist");
  }

  @Test
  void testFindNullParams() {
    this.registerPlayer(this.player);
    assertNull(dataSource.find(null), "It should return null");
  }

}
