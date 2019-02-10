package io.chocorean.authmod.config;

import io.chocorean.authmod.AuthMod;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = AuthMod.MODID)
public class AuthModConfig {
  @Comment("Email will be asked to the player for registration and authentication")
  public static boolean emailRequired = false;

  @Comment(
      "If the player doesn't log in after this delay (the unit is the second), he will be kicked from the server.")
  public static int delay = 60;

  @Comment(
      "Enable or disable the /login command (if disabled, the server will be opened to everyone)")
  public static boolean enableAuthentication = true;

  @Comment("Enable or disable the /register command")
  public static boolean enableRegistration = true;

  public enum Strategies {
    FILE,
    DATABASE
  }

  @Comment({
    "The way you want to store player's data, choose between 'database' or 'file'.",
    "If the strategy is unknown, the server will be open for everyone."
  })
  public static Strategies dataSourceStrategy = Strategies.FILE;

  public static final DatabaseConfig database = new DatabaseConfig();

  public static final class DatabaseConfig {
    @Comment("Column name for the email address")
    public String emailField = "email";

    @Comment("Column name telling whether the player is banned or not")
    public String bannedField = "banned";

    @Comment("Column name for the username")
    public String usernameField = "username";

    @Comment({"Column name for the player's uuid"})
    public String uuidField = "uuid";

    @Comment("Column name containing the encrypted password")
    public String passwordField = "password";

    @Comment("Name of the database")
    public String database = "minecraft";

    @Comment("SQL dialect used")
    public String dialect = "mariadb";

    @Comment("Host hosting the database")
    public String host = "mariadb";

    @Comment("Database user")
    public String user = "root";

    @Comment("Database user's password")
    public String password = "root";

    @Comment("Port to be used")
    public int port = 3306;

    @Comment("SQL table to be used")
    public String table = "players";
  }
}
