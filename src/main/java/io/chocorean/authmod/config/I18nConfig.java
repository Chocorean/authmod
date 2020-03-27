package io.chocorean.authmod.config;

import jdk.nashorn.internal.objects.ArrayBufferView;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class I18nConfig {

  private final Map<String, ForgeConfigSpec.ConfigValue<String>> mappings;

  public I18nConfig(ForgeConfigSpec.Builder builder) {
    builder.push("I18n");
    this.mappings = new HashMap<>();
    this.put(builder, "error", "Message displayed when an unknown error occurs.");
    this.put(builder, "welcome", "Message displayed to a player when he/she joins the server.");
    this.put(builder, "banned", "Message displayed when a banned player tries to connect.");
    this.put(builder, "login.usage","Usage for /login");
    this.put(builder, "login.success","Message when the player successfully signed in.");
    this.put(builder, "login.wrongPassword", "Message when the player entered a wrong password.");
    this.put(builder, "register.exist","Message when a player tries to sign up but am account already exists.");
    this.put(builder, "register.usage","Usage for /register");
    this.put(builder, "register.success","Message when the player successfully registered.");
    this.put(builder, "register.wrongPassword","The passwords doesn't match. Please retry");
    this.put(builder, "logged.yes","Message when the player is logged");
    this.put(builder, "logged.no","Message when the player is not logged");
    this.put(builder, "logged.no","Usage for /logged");
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
    Map<String, String> f = this.mappings.entrySet().stream()
      .filter(e -> !e.getValue().get().isEmpty())
      .collect(Collectors.toMap(Map.Entry::getKey,
      e -> e.getValue().get()));
    return f;
  }
}
