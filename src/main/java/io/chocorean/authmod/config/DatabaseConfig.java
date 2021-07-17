package io.chocorean.authmod.config;

import net.minecraftforge.common.config.Config;

public class DatabaseConfig {

  @Config.Comment("Column name telling whether the player is banned or not")
  public String columnBanned = "banned";

  @Config.Comment("Column name for the username")
  public String columnUsername = "username";

  @Config.Comment({ "Column name for the player's uuid" })
  public String columnUuid = "uuid";

  @Config.Comment("Column name containing the encrypted password")
  public String columnPassword = "password";

  @Config.Comment("Name of the database")
  public String database = "minecraft";

  @Config.Comment("SQL dialect used")
  public String dialect = "mariadb";

  @Config.Comment("Host hosting the database")
  public String host = "localhost";

  @Config.Comment("Database user")
  public String user = "user";

  @Config.Comment("Database user's password")
  public String password = "password";

  @Config.Comment("Port to be used")
  public int port = 3306;

  @Config.Comment("SQL table to be used")
  public String table = "players";

  @Config.Comment("JDBC driver to use")
  public String driver = "org.mariadb.jdbc.Driver";
}
