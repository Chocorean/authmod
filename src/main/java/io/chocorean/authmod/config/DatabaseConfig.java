package io.chocorean.authmod.config;

import io.chocorean.authmod.core.datasource.DatabaseStrategy;
import java.util.HashMap;
import java.util.Map;
import net.minecraftforge.common.ForgeConfigSpec;

public class DatabaseConfig {

  public final ForgeConfigSpec.ConfigValue<String> database;
  public final ForgeConfigSpec.ConfigValue<String> dialect;
  public final ForgeConfigSpec.ConfigValue<String> host;
  public final ForgeConfigSpec.ConfigValue<String> user;
  public final ForgeConfigSpec.ConfigValue<String> password;
  public final ForgeConfigSpec.IntValue port;
  public final ForgeConfigSpec.ConfigValue<String> table;
  public final ForgeConfigSpec.ConfigValue<String> driver;
  public final Map<DatabaseStrategy.Column, ForgeConfigSpec.ConfigValue<String>> columns = new HashMap<>();

  public DatabaseConfig(ForgeConfigSpec.Builder builder) {
    builder.push("Database");
    for (DatabaseStrategy.Column c : DatabaseStrategy.Column.values()) {
      String capitalized = c.name().substring(0, 1).toUpperCase() + c.name().substring(1).toLowerCase();
      this.columns.put(
          c,
          builder.comment(String.format("Column name of %s", c.name())).define(String.format("column%s", capitalized), c.name().toLowerCase())
        );
    }
    this.database = builder.comment("Name of the database").define("database", "minecraft");
    this.dialect = builder.comment("SQL dialect").define("dialect", "mariadb");
    this.host = builder.comment("Server hosting the database").define("host", "localhost");
    this.driver = builder.comment("JDBC driver to use").define("driver", "org.mariadb.jdbc.Driver");
    this.user = builder.comment("Database user").define("user", "user");
    this.password = builder.comment("Database users's password").define("password", "password");
    this.port = builder.comment("Port to be used").defineInRange("port", 3306, 1024, 65535);
    this.table = builder.comment("Table to be used").define("table", "players");
    builder.pop();
  }
}
