package io.chocorean.authmod.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.chocorean.authmod.AuthMod;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class I18nConfigTest {

  private Path copied;

  @BeforeEach
  void setup() throws IOException {
    String configFile = String.format("%s-server.toml", AuthMod.MODID);
    Path original = Paths.get("src", "main", "resources", configFile).toAbsolutePath();
    this.copied = File.createTempFile("i18n", configFile).toPath();
    Files.copy(original, this.copied, StandardCopyOption.REPLACE_EXISTING);
  }

  private void replaceInFile(Path path, String regex, String replacement) throws IOException {
    Charset charset = StandardCharsets.UTF_8;
    String content = new String(Files.readAllBytes(path), charset);
    content = content.replaceAll(regex, replacement);
    Files.write(path, content.getBytes(charset));
  }

  @Test
  public void testGetTranslations() {
    Config.loadConfig(copied);
    assertTrue(Config.i18n.getTranslations().isEmpty());
  }

  @Test
  public void testGetTranslationsOverwrite2keys() throws IOException {
    Map<String, String> newTranslations = new HashMap<>();
    newTranslations.put("authmod.changepassword.samePassword", "It does exist");
    newTranslations.put("authmod.register.exist", "Not the same password");
    for (Map.Entry<String, String> entry : newTranslations.entrySet()) {
      String[] parts = entry.getKey().split("\\.");
      String key = parts[parts.length - 1];
      this.replaceInFile(this.copied, String.format("%s = .*", key), String.format("%s = \"%s\"", key, entry.getValue()));
    }
    Config.loadConfig(copied);
    assertEquals(Config.i18n.getTranslations(), newTranslations);
  }
}
