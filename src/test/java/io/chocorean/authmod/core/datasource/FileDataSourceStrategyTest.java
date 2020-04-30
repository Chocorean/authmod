package io.chocorean.authmod.core.datasource;


import io.chocorean.authmod.core.Player;
import io.chocorean.authmod.core.PlayerInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class FileDataSourceStrategyTest {

  private static File file;
  private DataSourcePlayerInterface player;
  private DataSourceStrategyInterface dataSource;

  @BeforeEach
  void init() throws Exception {
    this.file = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
    this.dataSource = new FileDataSourceStrategy(this.file);
    clean();
    this.player = new DataSourcePlayer(new Player().setUsername("Whitney"));
  }

  static void clean() throws Exception {
    if (file.exists()) {
      Files.walk(file.toPath())
        .sorted(Comparator.reverseOrder())
        .map(Path::toFile)
        .forEach(File::delete);
    }
  }

  public boolean registerPlayer(PlayerInterface player) {
    DataSourcePlayerInterface playerToRegister = new DataSourcePlayer(player);
    return dataSource.add(playerToRegister);
  }

  @Test
  void testConstructor() throws Exception {
    new FileDataSourceStrategy(this.file);
    assertTrue(this.file.exists(), "The strategy should create a CSV file automatically");
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

  @Test
  void testSaveFileFailed() throws Exception {
    this.file.delete();
    this.file.mkdirs();
    boolean res = this.registerPlayer(this.player);
    clean();
    assertFalse(res);
  }

  @Test
  void testUpdateNotExist() {
    boolean res = this.dataSource.update(new DataSourcePlayer());
    assertFalse(res);
  }

  @Test
  void testUpdateFileFailed() throws Exception {
    this.file.delete();
    this.file.mkdirs();
    boolean res = this.dataSource.update(null);
    clean();
    assertFalse(res);
  }

  @Test
  void testWrongDataFile() throws Exception {
    file = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
    BufferedWriter bw = new BufferedWriter(new FileWriter(this.file));
    bw.write("test, test");
    bw.close();
    bw.close();
    this.dataSource = new FileDataSourceStrategy(file);
    assertFalse(this.dataSource.exist(new DataSourcePlayer(new Player("test", null))));
  }

}
