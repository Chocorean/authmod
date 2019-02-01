package io.chocorean.authmod.config;

import io.chocorean.authmod.AuthMod;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AuthModConfig {

    private final Configuration config;
    private static final String GEN_CATEGORY = "general";
    private static final String MSG_CATEGORY = "messages";
    private final AuthModDatabaseConfig databaseConfig;

    private boolean isLoginEnabled;
    private boolean isRegisterEnabled;
    private String strategy;
    private String hostedDomain;
    private String contact;
    private int delay;
    private boolean emailOnLogin;

    private String welcomeMsg;
    private String successMsg;
    private String wrongPasswordMsg;
    private String wrongUsernameMsg;
    private String notValidEmailMsg;
    private String databaseErrorMsg;
    private String bannedMsg;
    private String playerNotFoundMsg;
    private String playerAlreadyExistsMsg;
    private String playerAlreadyLoggedMsg;
    private String wrongNumberOfArgsMsg;
    private String registerUsageMsg;
    private String loginUsageMsg;
    private Map<String, String> defaultValues;
    private Map<String, String> comments;

    public AuthModConfig(File config) {
        this(new Configuration(config));
    }

    private AuthModConfig(Configuration config) {
        this.defaultValues = new HashMap<>();
        this.comments = new HashMap<>();
        this.config = config;
        this.databaseConfig = new AuthModDatabaseConfig(this);
        this.loadConfigurationData();
        this.config.load();
        this.loadProperties();
    }

    private void loadConfigurationData() {
        Properties props = new Properties();
        InputStream inputStream = getClass().getResourceAsStream("/authmod-configuration.properties");
        try {
            props.load(inputStream);
            props.forEach((key, value) -> {
                if(key.toString().endsWith(".comment")) {
                    this.comments.put(key.toString(), value.toString());
                }
                if(key.toString().endsWith(".default")) {
                    this.defaultValues.put(key.toString(), value.toString());
                }
            });
        } catch (IOException e) {
            AuthMod.LOGGER.catching(e);
        }
    }

    void loadProperties() {
        this.config.addCustomCategoryComment("database",
                " ______            __     __                                __\n" +
                        "/\\  _  \\          /\\ \\__ /\\ \\                              /\\ \\\n" +
                        "\\ \\ \\L\\ \\   __  __\\ \\ ,_\\\\ \\ \\___      ___ ___      ___    \\_\\ \\\n" +
                        " \\ \\  __ \\ /\\ \\/\\ \\\\ \\ \\/ \\ \\  _ `\\  /' __` __`\\   / __`\\  /'_` \\\n" +
                        "  \\ \\ \\/\\ \\\\ \\ \\_\\ \\\\ \\ \\_ \\ \\ \\ \\ \\ /\\ \\/\\ \\/\\ \\ /\\ \\L\\ \\/\\ \\L\\ \\\n" +
                        "   \\ \\_\\ \\_\\\\ \\____/ \\ \\__\\ \\ \\_\\ \\_\\\\ \\_\\ \\_\\ \\_\\\\ \\____/\\ \\___,_\\\n" +
                        "    \\/_/\\/_/ \\/___/   \\/__/  \\/_/\\/_/ \\/_/\\/_/\\/_/ \\/___/  \\/__,_ /\n" +
                        "                                                       version " + AuthMod.VERSION +"\n" +
                        " Github link\n" +
                        "  - https://github.com/Chocorean/authmod\n" +
                        " Authors\n" +
                        "   - Chocorean\n" +
                        "   - Mcdostone");
        this.isLoginEnabled = this.getProperty(AuthModConfig.GEN_CATEGORY, "isLoginEnabled").getBoolean();
        this.isRegisterEnabled = this.getProperty(AuthModConfig.GEN_CATEGORY, "isRegisterEnabled").getBoolean();
        this.strategy = this.getProperty(AuthModConfig.GEN_CATEGORY,"strategy").getString();
        this.hostedDomain = this.getProperty(AuthModConfig.GEN_CATEGORY,"hostedDomain").getString();
        this.contact = this.getProperty(AuthModConfig.GEN_CATEGORY,"contact").getString();
        this.delay = this.getProperty(AuthModConfig.GEN_CATEGORY,"delay").getInt();
        this.emailOnLogin = this.getProperty(AuthModConfig.GEN_CATEGORY,"emailOnLogin").getBoolean();

        this.welcomeMsg = this.getProperty(AuthModConfig.MSG_CATEGORY,"welcome").getString();
        this.successMsg = this.getProperty(AuthModConfig.MSG_CATEGORY,"success").getString();
        this.wrongPasswordMsg = this.getProperty(AuthModConfig.MSG_CATEGORY,"wrongPassword").getString();
        this.wrongUsernameMsg = this.getProperty(AuthModConfig.MSG_CATEGORY,"wrongUsername").getString();
        this.notValidEmailMsg = this.getProperty(AuthModConfig.MSG_CATEGORY,"notValidEmail").getString();
        this.databaseErrorMsg = this.getProperty(AuthModConfig.MSG_CATEGORY,"databaseError").getString();
        this.bannedMsg = this.getProperty(AuthModConfig.MSG_CATEGORY,"banned").getString();
        this.playerNotFoundMsg = this.getProperty(AuthModConfig.MSG_CATEGORY,"playerNotFound").getString();
        this.playerAlreadyExistsMsg = this.getProperty(AuthModConfig.MSG_CATEGORY,"playerAlreadyExists").getString();
        this.playerAlreadyLoggedMsg = this.getProperty(AuthModConfig.MSG_CATEGORY,"playerAlreadyLogged").getString();
        this.wrongNumberOfArgsMsg = this.getProperty(AuthModConfig.MSG_CATEGORY,"wrongNumberOfArgs").getString();
        this.registerUsageMsg = this.getProperty(AuthModConfig.MSG_CATEGORY,"registerUsage").getString();
        this.loginUsageMsg = this.getProperty(AuthModConfig.MSG_CATEGORY,"loginUsage").getString();

        this.databaseConfig.loadProperties();
        if (this.config.hasChanged())
            this.config.save();
    }


    public Property getProperty(String category, String key) {
        return this.config.get(category,
                key,
                this.getDefaultValue(category, key),
                this.getComment(category, key));
    }

    private String getDefaultValue(String category, String key) {
        return this.defaultValues.get(String.format("%s.%s.default", category.trim().toLowerCase(), key.trim()));
    }

    private String getComment(String category, String key) {
        return this.comments.get(String.format("%s.%s.comment", category.trim().toLowerCase(), key.trim()));
    }

    public AuthModDatabaseConfig getDatabaseConfig() {
        return this.databaseConfig;
    }

    public String getAuthenticationStrategy() {
        return this.strategy;
    }

    public String getContact() {
        return this.contact;
    }

    public String getHostedDomain() {
        return this.hostedDomain;
    }

    public boolean getEmailOnLogin() {
        return this.emailOnLogin;
    }

    public boolean isLoginEnabled() {
        return this.isLoginEnabled;
    }

    public boolean isRegisterEnabled() {
        return this.isRegisterEnabled;
    }

    public int getDelay() {
        return this.delay;
    }

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
