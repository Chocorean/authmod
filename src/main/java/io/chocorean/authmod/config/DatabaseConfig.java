package io.chocorean.authmod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class DatabaseConfig {

  public final ForgeConfigSpec.ConfigValue<String> columnIdentifier;
  public final ForgeConfigSpec.ConfigValue<String> columnBan;
  public final ForgeConfigSpec.ConfigValue<String> columnUsername;
  public final ForgeConfigSpec.ConfigValue<String> columnUuid;
  public final ForgeConfigSpec.ConfigValue<String> columnPassword;
  public final ForgeConfigSpec.ConfigValue<String> database;
  public final ForgeConfigSpec.ConfigValue<String> dialect;
  public final ForgeConfigSpec.ConfigValue<String> host;
  public final ForgeConfigSpec.ConfigValue<String> user;
  public final ForgeConfigSpec.ConfigValue<String> password;
  public final ForgeConfigSpec.IntValue port;
  public final ForgeConfigSpec.ConfigValue<String> table;

  public DatabaseConfig(ForgeConfigSpec.Builder builder) {
    builder.push("Database");
    this.columnIdentifier = builder
      .comment("Column for the identifier")
      .define("columnIdentifier", "email");

    this.columnBan = builder
      .comment("Column telling whether the player is banned")
      .define("columnBan", "banned");

    this.columnUsername = builder
      .comment("Column for the username")
      .define("colummUsername", "username");

    this.columnUuid = builder
      .comment("Column for UUIF")
      .define("colummUuid", "uuid");

    this.columnPassword = builder
      .comment("Column for the encrypted password")
      .define("columnPassword", "password");

    this.database = builder
      .comment("Name of the database")
      .define("database", "minecraft");

    this.dialect = builder
      .comment("SQL dialect")
      .define("dialect", "mariadb");

    this.host = builder
      .comment("Server hosting the database")
      .define("host", "localhost");

    this.user = builder
      .comment("Database user")
      .define("user", "user");

    this.password = builder
      .comment("Database users's password")
      .define("password", "user");

    this.port = builder
      .comment("Port to be used")
      .defineInRange("port",3306,1024,65535);

    this.table = builder
      .comment("Table to be used")
      .define("table","players");


    builder.pop();
  }
}
