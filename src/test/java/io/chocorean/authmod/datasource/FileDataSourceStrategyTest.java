package io.chocorean.authmod.datasource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.chocorean.authmod.PlayerFactory;
import io.chocorean.authmod.exception.AuthmodException;
import io.chocorean.authmod.exception.PlayerAlreadyExistException;
import io.chocorean.authmod.exception.RegistrationException;
import io.chocorean.authmod.guard.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.guard.datasource.IDataSourceStrategy;
import io.chocorean.authmod.model.IPlayer;

class FileDataSourceStrategyTest {
  private File dataFile;
  private IPlayer player;
  private IDataSourceStrategy dataSource;

  @BeforeEach
  void init() throws IOException {
	this.dataFile = File.createTempFile(System.getProperty("java.io.tmpdir"), "authmod.csv");
    if (this.dataFile.exists()) {
      assert this.dataFile.delete();
    }
  }

  private boolean registerPlayer() throws RegistrationException {
    this.dataSource = new FileDataSourceStrategy(this.dataFile);
    this.player = PlayerFactory.create();
    return dataSource.add(this.player);
  }

  @Test
  void testConstructor() {
    new FileDataSourceStrategy(this.dataFile);
    assertTrue(this.dataFile.exists(), "The strategy should create a CSV file automatically");
  }

  @Test
  void testAdd() throws AuthmodException {
    boolean added = this.registerPlayer();
    assertTrue(added, "The player should be registered");
  }

  @Test
  void testAddDoublon() throws AuthmodException {
    boolean added = this.registerPlayer();
    assertTrue(added, "The player should be registered");
    assertThrows(PlayerAlreadyExistException.class, this::registerPlayer);
  }

  @Test
  void testFileModified() throws RegistrationException, IOException, InterruptedException {
    this.registerPlayer();
    Thread.sleep(1000);
    BufferedWriter writer = new BufferedWriter(new FileWriter(this.dataFile, true));
    writer.append("test@test.fr, mcdostone, password, false");
    writer.flush();
    writer.close();
    assertNotNull(this.dataSource.find(null, "mcdostone"));
  }

  @Test
  void testFindByEmail() throws RegistrationException {
    this.registerPlayer();
    assertNotNull(
        dataSource.find(this.player.getEmail(), null), "The player should exist and be found");
  }

  @Test
  void testFindByUsername() throws RegistrationException {
    this.registerPlayer();
    assertNotNull(
        dataSource.find(null, player.getUsername()), "The player should exist and be found");
  }

  @Test
  void testFindNotExist() throws RegistrationException {
    this.registerPlayer();
    assertNull(dataSource.find("test@test.fr", "test"), "The player should not exist");
  }

  @Test
  void testFindByUsernameOrEmail() throws RegistrationException {
    this.registerPlayer();
    assertNotNull(dataSource.find(player.getEmail(), player.getUsername()),"The player should exist and be found");
  }

  @Test
  void testFindNullParams() throws AuthmodException {
    this.registerPlayer();
    assertNull(dataSource.find(null, null), "It should return null");
  }
}
