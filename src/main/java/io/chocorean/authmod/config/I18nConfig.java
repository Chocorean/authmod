package io.chocorean.authmod.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.util.text.ServerLanguageMap;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class I18nConfig {

  private final Map<String, ForgeConfigSpec.ConfigValue<String>> mappings;

  public I18nConfig(ForgeConfigSpec.Builder builder) {
    builder.push("I18n");
    this.mappings = new HashMap<>();
    String path = "/assets/authmod/lang/en_us.json";
    try (InputStream inputstream = ServerLanguageMap.class.getResourceAsStream(path)) {
      JsonElement jsonelement = (new Gson()).fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), JsonElement.class);
      JsonObject jsonobject = JSONUtils.getJsonObject(jsonelement, "strings");
      for(Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
        String s = ServerLanguageMap.NUMERIC_VARIABLE_PATTERN.matcher(JSONUtils.getString(entry.getValue(), entry.getKey())).replaceAll("%$1s");
        this.put(builder, entry.getKey(), s);
      }
    } catch (Exception exception) {
      AuthMod.LOGGER.catching(exception);
    }
    builder.pop();
  }

  private void put(ForgeConfigSpec.Builder builder, String key, String comment) {
    key = key.startsWith("authmod") ? key : "authmod.".concat(key);
    String[] tmp = key.replace("authmod.", "").split("\\.");
    String name = IntStream.range(0, tmp.length)
      .mapToObj(i -> i == 0 ? tmp[0] : tmp[i].substring(0,1).toUpperCase() + tmp[i].substring(1).toLowerCase())
      .collect(Collectors.joining());
    this.mappings.put(key, builder
      .comment(comment)
      .define(name, ""));
  }

  public Map<String, String> getTranslations() {
    return this.mappings.entrySet().stream()
      .filter(e -> !e.getValue().get().isEmpty())
      .collect(Collectors.toMap(Map.Entry::getKey,
      e -> e.getValue().get()));
  }
}
