package io.chocorean.authmod.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class AuthModDatabaseConfig {

    private final Configuration config;
    private static final String DB_CATEGORY = "database";

    // database properties
    private String dialect = "mariadb";
    private String host = "mariadb";
    private String port = "3306";
    private String database = "minecraft";
    private String user = "root";
    private String password = "root";
    private String table = "players";

    public AuthModDatabaseConfig(Configuration config) {
        this.config = config;
    }

    public void loadProperties() {
        try {
            Property dialectProp = config.get(DB_CATEGORY,
                    "dialect",
                    "mariadb",
                    "Dialect you want to be used by Authmod.");
            dialect = dialectProp.getString();

            Property hostProp = config.get(DB_CATEGORY,
                    "host",
                    "mariadb",
                    "Name of the device hosting the database.");
            host = hostProp.getString();

            Property portProp = config.get(DB_CATEGORY,
                    "port",
                    "3306",
                    "Port to be used.");
            port = portProp.getString();

            Property databaseProp = config.get(DB_CATEGORY,
                    "database",
                    "minecraft",
                    "Name of database you are using.");
            database = databaseProp.getString();


            Property userProp = config.get(DB_CATEGORY,
                    "user",
                    "root",
                    "Database user.");
            user = userProp.getString();


            Property passwdProp = config.get(DB_CATEGORY,
                    "password",
                    "root",
                    "Database user's password.");
            password = passwdProp.getString();


            Property tableProp = config.get(DB_CATEGORY,
                    "table",
                    "players",
                    "Name of the table to use.");
            table = tableProp.getString();

        } catch (Exception e) {
            // keep reading
        }
    }

    //private String get(String key, String def) { return this.config.get(DB_CATEGORY, key, def).getString(); }

    public String getUser() { return this.user; }

    public String getDatabase() { return this.database; }

    public String getPassword() { return this.password; }

    public String getHost() { return this.host; }

    public String getDialect() { return this.dialect; }

    public String getTable() { return this.table; }

    public String getPort() { return this.port; }

}

