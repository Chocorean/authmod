package io.chocorean.authmod.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.chocorean.authmod.AuthMod;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class ConfigTest {

  private final Path file = Paths
    .get("src", "main", "resources", String.format("%s-server.toml", AuthMod.MODID))
    .toAbsolutePath();

  @Test
  public void testLoadConfig() {
    Config.loadConfig(this.file);
  }

  @Test
  public void testAuthmodEnabled() {
    Config.loadConfig(this.file);
    assertFalse(Config.authmodEnabled());
  }

  @Test
  public void testOnReload() {
    Config.loadConfig(this.file);
    Config.onReload(null);
    assertEquals(Config.delay.get(), 60);
  }

  @Test
  public void testOnLoad() {
    Config.loadConfig(this.file);
    Config.onLoad(null);
  }
}
