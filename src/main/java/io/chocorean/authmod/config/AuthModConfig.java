package io.chocorean.authmod.config;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class AuthModConfig {

    private Configuration config;
    private final String GEN_CATEGORY = "general";
    private AuthModDatabaseConfig databaseConfig;

    public AuthModConfig(Configuration config) {
        this.config = config;
        this.databaseConfig = new AuthModDatabaseConfig(config);
    }

    public AuthModConfig(File config) {
        this(new Configuration(config));
    }

    public void load() {
        this.config.load();
    }

    public void save() {
        this.config.save();
    }

    public String getAuthenticationStrategy() {
        return this.config.get(GEN_CATEGORY, "strategy", "file").getString();
    }

    public String getContact() {
        return this.config.get(GEN_CATEGORY, "contact", "admins").getString();
    }

    public String getMessage() {
        return this.config.get(GEN_CATEGORY, "message", "Use /login to start playing!").getString();
    }

    public String getWebsite() { return this.config.get(GEN_CATEGORY, "website", "files.minecraftforge.net").getString(); }

    public boolean isAuthenticationEnabled() {
        return this.config.get(GEN_CATEGORY, "enableAuth", true).getBoolean();
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

    public class AuthModDatabaseConfig {
        private final Configuration config;
        private final String DB_CATEGORY = "database";

        public AuthModDatabaseConfig(Configuration config) { this.config = config; }

        private String get(String key, String def) { return this.config.get(DB_CATEGORY, key, def).getString(); }

        public String getUser() { return this.get("user", "root"); }
        public String getName() { return this.get("name", "minecraft"); }
        public String getPassword() { return this.get("password", "root"); }
        public String getHost() { return this.get("host", "127.0.0.1"); }
        public String getDialect() { return this.get("dialect", "mariadb"); }
    }






}
