package io.chocorean.authmod.config;

import com.ibm.icu.impl.Normalizer2Impl;
import io.chocorean.authmod.AuthMod;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = AuthMod.MODID)
public class AuthModConfig {
  @Comment("Identifier (username, email, whatever...) will be asked to the player for registration and authentication")
  public static boolean identifierRequired = false;

  @Comment("If the player doesn't log in after this delay (the unit is the second), he will be kicked from the server.")
  public static int delay = 60;

  @Comment("Enable or disable the /login command (if disabled, the server will be opened to everyone)")
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

  public static final I18nConfig i18n = new I18nConfig();

  public static final class DatabaseConfig {
    @Comment("Column name for the identifier column")
    public String identifierField = "email";

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

  public static final class I18nConfig {
    @Comment("Message displayed when an error with database occurs. Check your server logs.")
    public String error = "Something was wrong. Please contact the admins.";

    @Comment("Message displayed to a player when he/she joins the server.")
    public String welcome = "Use /register to sign up or /login to sign in.";

    public String delay = "Wake up, you have only %s seconds to log in.";


    @Comment("Message displayed when a player tries to connect while being banned.")
    public String loginBanned = "You've been banned. Please contact the admins.";

    @Comment("Usage for /login")
    public String loginUsage = "/login <email> <password> - Allows you to authenticate on the server";

    @Comment("Message displayed to a player when he/she attempts to sign in with an incorrect email.")
    public String loginInvalidEmail = "Your email is not valid.";

    @Comment("Message displayed when a player tries to connect without having registered.")
    public String loginUnknown = "%s is not registered on this server.";

    @Comment("Message displayed to a player when he/she successfully signs in.")
    public String loginSuccess = "Have fun!";

    @Comment("Message displayed to a player when he/she typed a wrong password.")
    public String loginWrongPassword = "Wrong password. Please try again.";

    @Comment("Message displayed to a player when he/she typed a wrong password.")
    public String loginWrongUuid = "Wrong UUID. Please be sure you use the same UUID when you registered your account.";

    @Comment("Message displayed to a player when he/she attempts to sign in with wrong username")
    public String loginWrongUsername = "Your username does not correspond to your credentials.";

    @Comment("Message displayed when a player tries to sign up with an already-registered account.")
    public String registerExist = "Someone has already registered with this username or email.";

    @Comment("Usage for /register")
    public String registerUsage = "/register <email> <password> <password>- Be careful when choosing it, you'll be asked to login each time you play.";

    @Comment("Alternative usage for /register")
    public String registerAlternativeUsage = "/register <password> <password>- Be careful when choosing it, you'll be asked to login each time you play.";

    @Comment("Message displayed to a player when he/she successfully registered.")
    public String registerSuccess = "You are registered and authenticated, have fun!";

    @Comment("Message displayed to a player when he/she fails to login.")
    public String registerWrongPasswordConfirmation = "The password confirmation doesn't match. Please retry.";

    @Comment("Message displayed to a player when he/she tries to register while being already logged in.")
    public String registerAlreadyLogged = "You are already logged in, no need to register !";

    @Comment("Message displayed to a player when he/she tries to register with a short password.")
    public String registerPasswordTooShort = "Your password should be at least 5-character long.";

    public String loggedYes = "Yes";
    public String loggedNo = "No";

    @Comment("Usage for /logged")
    public String loggedUsage = "/logged - tells you whether you are authenticated or not";
  }
}
