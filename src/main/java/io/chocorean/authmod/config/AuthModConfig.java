package io.chocorean.authmod.config;

import io.chocorean.authmod.AuthMod;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.io.IOException;

public class AuthModConfig {

    private final Configuration config;
    private static final int MINIMUM_DELAY = 10;
    private static final String GEN_CATEGORY = "general";
    private static final String MSG_CATEGORY = "message";
    private final AuthModDatabaseConfig databaseConfig;

    // general properties
    private boolean isLoginEnabled = true;
    private boolean isRegisterEnabled = true;
    private String strategy = "file";
    private String website = "www.example-website.com";
    private String hostedDomain = "";
    private String contact = "";
    private int delay = 60;

    // message properties
    private String welcomeMsg = "";
    private String successMsg = "";
    private String wrongPasswordMsg = "";
    private String wrongUsernameMsg = "";
    private String notValidEmailMsg = "";
    private String databaseErrorMsg = "";
    private String bannedMsg = "";
    private String playerNotFoundMsg = "";
    private String playerAlreadyExistsMsg = "";
    private String playerAlreadyLoggedMsg = "";
    private String wrongNumberOfArgsMsg = "";
    private String registerUsageMsg = "";
    private String loginUsageMsg = "";


    public AuthModConfig(File config) {
        this(new Configuration(config));
    }

    private AuthModConfig(Configuration config) {
        this.config = config;
        this.databaseConfig = new AuthModDatabaseConfig(config);
        this.config.load();

        config.addCustomCategoryComment(GEN_CATEGORY, " ______            __     __                                __\n" +
                "/\\  _  \\          /\\ \\__ /\\ \\                              /\\ \\\n" +
                "\\ \\ \\L\\ \\   __  __\\ \\ ,_\\\\ \\ \\___      ___ ___      ___    \\_\\ \\\n" +
                " \\ \\  __ \\ /\\ \\/\\ \\\\ \\ \\/ \\ \\  _ `\\  /' __` __`\\   / __`\\  /'_` \\\n" +
                "  \\ \\ \\/\\ \\\\ \\ \\_\\ \\\\ \\ \\_ \\ \\ \\ \\ \\ /\\ \\/\\ \\/\\ \\ /\\ \\L\\ \\/\\ \\L\\ \\\n" +
                "   \\ \\_\\ \\_\\\\ \\____/ \\ \\__\\ \\ \\_\\ \\_\\\\ \\_\\ \\_\\ \\_\\\\ \\____/\\ \\___,_\\\n" +
                "    \\/_/\\/_/ \\/___/   \\/__/  \\/_/\\/_/ \\/_/\\/_/\\/_/ \\/___/  \\/__,_ /\n" +
                "                                                       version 2.3\n" +
                " Github link\n" +
                "  - https://github.com/Chocorean/authmod\n" +
                " Authors\n" +
                "   - Chocorean\n" +
                "   - Mcdostone");

        try { // Read props from config
            /* general category */
            Property isLoginEnabledProp = config.get(AuthModConfig.GEN_CATEGORY,
                    "isLoginEnabled",
                    "true",
                    "Enable or disable authentication layer on the server");
            isLoginEnabled = isLoginEnabledProp.getBoolean();

            Property isRegisterEnabledProp = config.get(AuthModConfig.GEN_CATEGORY,
                    "isRegisterEnabled",
                    "true",
                    "Enable or disable players to register themselves");
            isRegisterEnabled = isRegisterEnabledProp.getBoolean();

            Property strategyProp = config.get(AuthModConfig.GEN_CATEGORY,
                    "strategy",
                    "file",
                    "Choose your authentication strategy between 'database' or 'file'.\nIf the strategy is unknown, the server will be open for everyone.");
            strategy = strategyProp.getString();

            Property websiteProp = config.get(AuthModConfig.GEN_CATEGORY,
                    "website",
                    "www.example-website.com",
                    "Website to display if the player can't log in");
            website = websiteProp.getString();

            Property hostedDomainProp = config.get(AuthModConfig.GEN_CATEGORY,
                    "hostedDomain",
                    "",
                    "Hosted domain");
            hostedDomain = hostedDomainProp.getString();

            Property contactProp = config.get(AuthModConfig.GEN_CATEGORY,
                    "contact",
                    "the admins",
                    "People to contact if an issue is met.");
            contact = contactProp.getString();

            Property delayProp = config.get(AuthModConfig.GEN_CATEGORY,
                    "delay",
                    60, // Default value
                    "If the player doesn't log in after this delay (the unit is the second), he will be kicked from the server.");
            delay = delayProp.getInt();

            /* message category */
            Property welcomeMsgProp = config.get(AuthModConfig.MSG_CATEGORY,
                    "welcomeMsg",
                    isRegisterEnabled ? "Use /register to sign up or /login to sign in." : "Use /login to sign in.",
                    "Message displayed to a player when he/she joins the server.\nMake sure it is clear enough to guide them.");
            welcomeMsg = welcomeMsgProp.getString();

            Property successMsgProp = config.get(AuthModConfig.MSG_CATEGORY,
                    "successMsg",
                    "You've successfully signed in.",
                    "Message displayed to a player when he/she successfully signs in.");
            successMsg = successMsgProp.getString();

            Property wrongPasswordMsgProp = config.get(AuthModConfig.MSG_CATEGORY,
                    "wrongPasswordMsg",
                    "Wrong password. Please try again.",
                    "Message displayed to a player when he/she typed a wrong password.");
            wrongPasswordMsg = wrongPasswordMsgProp.getString();

            Property wrongUsernameMsgProp = config.get(AuthModConfig.MSG_CATEGORY,
                    "wrongUsernameMsg",
                    "Your username does not correspond to your credentials.",
                    "Message displayed to a player when he/she attemps to sign in with wrong username.");
            wrongUsernameMsg = wrongUsernameMsgProp.getString();

            Property notValidEmailMsgProp = config.get(AuthModConfig.MSG_CATEGORY,
                    "notValidEmailMsg",
                    "Your email is not valid.",
                    "Message displayed to a player when he/she attemps to sign in with an incorrect email.");
            notValidEmailMsg = notValidEmailMsgProp.getString();

            Property databaseErrorMsgProp = config.get(AuthModConfig.MSG_CATEGORY,
                    "databaseErrorMsg",
                    "Something when wrong. Please contact " + this.getContact()+".",
                    "Message displayed when an error with database occurs. Check your server logs.");
            databaseErrorMsg = databaseErrorMsgProp.getString();

            Property bannedMsgProp = config.get(AuthModConfig.MSG_CATEGORY,
                    "bannedMsg",
                    "You've been banned. Please contact " + this.getContact()+".",
                    "Message displayed when a player tries to connect while being banned.");
            bannedMsg = bannedMsgProp.getString();

            Property playerNotFoundMsgProp = config.get(AuthModConfig.MSG_CATEGORY,
                    "playerNotFoundMsg",
                    "You've not registered yet. Please visit "+this.getWebsite(),
                    "Message displayed when a player tries to connect without having registered.");
            playerNotFoundMsg = playerNotFoundMsgProp.getString();

            Property playerAlreadyExistsProp = config.get(AuthModConfig.MSG_CATEGORY,
                    "playerAlreadyExistsMsg",
                    "Someone has already registered with this username or email.",
                    "Message displayed when a player tries to sign up with an already-registered account.");
            playerAlreadyExistsMsg = playerAlreadyExistsProp.getString();

            Property playerAlreadyLoggedProp = config.get(AuthModConfig.MSG_CATEGORY,
                    "playerAlreadyLoggedMsg",
                    "You are already logged in.",
                    "Message displayed when an already-logged player tries to sign in.");
            playerAlreadyLoggedMsg = playerAlreadyLoggedProp.getString();

            Property wrongNumberOfArgsProp = config.get(AuthModConfig.MSG_CATEGORY,
                    "wrongNumberOfArgsMsg",
                    "You need to type /[register|login] <email> <password>",
                    "Message displayed when a player do not use correctly /login or /register");
            wrongNumberOfArgsMsg = wrongNumberOfArgsProp.getString();

            Property registerUsageProp = config.get(AuthModConfig.MSG_CATEGORY,
                    "registerUsageMsg",
                    "/register <email> <password> - Be careful when choosing it, you'll be asked to login each time you play.",
                    "Usage for /register");
            registerUsageMsg = registerUsageProp.getString();

            Property loginUsageProp = config.get(AuthModConfig.MSG_CATEGORY,
                    "loginUsageMsg",
                    "/login <email> <password> - Allows you to authenticate on the server",
                    "Usage for /login");
            loginUsageMsg = loginUsageProp.getString();

        } catch (Exception e) {
            // keep reading
        } finally {
            this.databaseConfig.loadProperties();
            if (this.config.hasChanged()) this.config.save();
        }
    }

    public AuthModDatabaseConfig getDatabaseConfig() {
        return this.databaseConfig;
    }

    // general getters
    public String getAuthenticationStrategy() {
        return this.strategy;
    }

    public String getContact() {
        return this.contact;
    }

    public String getHostedDomain() {
        return this.hostedDomain;
    }

    public String getWebsite() {
        return this.website;
    }

    public boolean isLoginEnabled() {
        return this.isLoginEnabled;
    }

    public boolean isRegisterEnabled() {
        return this.isRegisterEnabled;
    }

    public int getDelay() {
        return this.delay < MINIMUM_DELAY ? MINIMUM_DELAY : this.delay;
    }

    // message getters
    public String getWelcomeMessage() {
        return this.welcomeMsg;
    }

    public String getSuccessMsg() {
        return this.successMsg;
    }

    public String getWrongPasswordMsg() {
        return wrongPasswordMsg;
    }

    public String getWrongUsernameMsg() {
        return wrongUsernameMsg;
    }

    public String getNotValidEmailMsg() {
        return notValidEmailMsg;
    }

    public String getDatabaseErrorMsg() {
        return databaseErrorMsg;
    }

    public String getBannedMsg() {
        return bannedMsg;
    }

    public String getPlayerNotFoundMsg() {
        return playerNotFoundMsg;
    }

    public String getPlayerAlreadyExistsMsg() {
        return playerAlreadyExistsMsg;
    }

    public String getPlayerAlreadyLoggedMsg() {
        return playerAlreadyLoggedMsg;
    }

    public String getWrongNumberOfArgsMsg() {
        return wrongNumberOfArgsMsg;
    }

    public String getRegisterUsageMsg() {
        return registerUsageMsg;
    }

    public String getLoginUsageMsg() {
        return loginUsageMsg;
    }
}
