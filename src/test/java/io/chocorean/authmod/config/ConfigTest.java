package io.chocorean.authmod.config;

import io.chocorean.authmod.AuthMod;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

  private final Path file = Paths
    .get("src", "main", "resources", String.format("%s-server.toml", AuthMod.MODID))
    .toAbsolutePath();

  @Test
  void testLoadConfig() {
    assertDoesNotThrow(() -> Config.loadConfig(this.file));
  }

  @Test
  void testAuthmodEnabled() {
    Config.loadConfig(this.file);
    assertFalse(Config.authmodEnabled());
  }

  @Test
  void testOnReload() {
    Config.loadConfig(this.file);
    Config.onReload(null);
    assertEquals(60, Config.delay.get());
  }

  @Test
  void testOnLoad() {
    Config.loadConfig(this.file);
    assertDoesNotThrow(() -> Config.onLoad(null));
  }
}
