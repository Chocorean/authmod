package io.chocorean.authmod.config;

import net.minecraftforge.common.config.Configuration;

public class AuthModDatabaseConfig {

    private final Configuration config;
    private static final String DB_CATEGORY = "database";

    public AuthModDatabaseConfig(Configuration config) { this.config = config; }

    private String get(String key, String def) { return this.config.get(DB_CATEGORY, key, def).getString(); }

    public String getUser() { return this.get("user", "root"); }

    public String getDatabase() { return this.get("name", "minecraft"); }

    public String getPassword() { return this.get("password", ""); }

    public String getHost() { return this.get("host", "127.0.0.1"); }

    public String getDialect() { return this.get("dialect", "mariadb"); }

    public String getTable() { return this.get("table", "players"); }

    public String getPort() { return this.get("port", ""); }

}

