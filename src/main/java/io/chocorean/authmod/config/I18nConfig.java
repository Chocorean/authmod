package io.chocorean.authmod.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.i18n.ServerLanguageMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraftforge.common.ForgeConfigSpec;

public class I18nConfig {

  private final Map<String, ForgeConfigSpec.ConfigValue<String>> mappings;

  public I18nConfig(ForgeConfigSpec.Builder builder) {
    builder.push("I18n");
    this.mappings = new HashMap<>();
    String path = String.format("/assets/%s/lang/en_us.json", AuthMod.MODID);
    try (InputStream inputstream = ServerLanguageMap.class.getResourceAsStream(path)) {
      JsonObject jsonObject = new Gson().fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), JsonObject.class);
      for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
        this.put(builder, entry.getKey(), entry.getValue().getAsString());
      }
    } catch (IOException e) {
      AuthMod.LOGGER.catching(e);
    }
    builder.pop();
  }

  private void put(ForgeConfigSpec.Builder builder, String key, String comment) {
    this.mappings.put(key, builder.comment(comment).define(key, ""));
  }

  public Map<String, String> getTranslations() {
    return this.mappings.entrySet()
      .stream()
      .filter(e -> !e.getValue().get().isEmpty())
      .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
  }
}
