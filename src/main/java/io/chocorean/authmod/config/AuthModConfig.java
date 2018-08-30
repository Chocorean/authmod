package io.chocorean.authmod.config;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class AuthModConfig {

    private final Configuration config;
    private final String GEN_CATEGORY = "general";
    private final AuthModDatabaseConfig databaseConfig;

    public AuthModConfig(Configuration config) {
        this.config = config;
        this.databaseConfig = new AuthModDatabaseConfig(config);
        this.config.load();
    }

    public AuthModConfig(File config) {
        this(new Configuration(config));
    }

    public void save() {
        this.config.save();
    }

    public String getAuthenticationStrategy() { return this.config.get(GEN_CATEGORY, "strategy", "file").getString(); }

    public String getContact() {
        return this.config.get(GEN_CATEGORY, "contact", "the admins").getString();
    }

    public String getMessage() { return this.config.get(GEN_CATEGORY, "message", "Use /login to start playing!").getString(); }

    public String getHostedDomain() { return this.config.get(GEN_CATEGORY, "hostedDomain", "").getString(); }

    public String getWebsite() { return this.config.get(GEN_CATEGORY, "website", "https://github.com/Chocorean/authmod").getString(); }

    public boolean isLoginEnabled() {
        return this.config.get(GEN_CATEGORY, "enableLogin", true).getBoolean();
    }

    public boolean isRegisterEnabled() {
        return this.config.get(GEN_CATEGORY, "enableRegister", true).getBoolean();
    }

    public int getDelay() {
        return this.config.get(GEN_CATEGORY, "delay", "60").getInt();
    }

    public AuthModDatabaseConfig getDatabaseConfig() {
        return this.databaseConfig;
    }

    private static AuthModConfig load(File file) {
        Configuration config =  new Configuration(file);
        return new AuthModConfig(config);
    }

}
