package io.chocorean.authmod.config;

import java.util.HashMap;
import java.util.Map;

public class AuthModDatabaseConfig {

    private final AuthModConfig config;
    private static final String DB_CATEGORY = "database";
    private String dialect;
    private String host;
    private int port;
    private String database;
    private String user;
    private String password;
    private String table;
    private Map<String, String> columns;

    AuthModDatabaseConfig(AuthModConfig config) {
        this.config = config;
        this.columns = new HashMap<>();
    }

    void loadProperties() {
        this.dialect = this.config.getProperty(DB_CATEGORY,"dialect").getString();
        this.host = this.config.getProperty(DB_CATEGORY,"host").getString();
        this.port = this.config.getProperty(DB_CATEGORY,"port").getInt();
        this.database = this.config.getProperty(DB_CATEGORY,"database").getString();
        this.user = this.config.getProperty(DB_CATEGORY, "user").getString();
        this.password = this.config.getProperty(DB_CATEGORY,"password").getString();
        this.table = this.config.getProperty(DB_CATEGORY,"table").getString();
        this.columns.put("email", this.config.getProperty(DB_CATEGORY,"columnEmail").getString());
        this.columns.put("banned", this.config.getProperty(DB_CATEGORY,"columnBanned").getString());
        this.columns.put("uuid", this.config.getProperty(DB_CATEGORY,"columnUuid").getString());
        this.columns.put("username", this.config.getProperty(DB_CATEGORY,"columnUsername").getString());
        this.columns.put("password", this.config.getProperty(DB_CATEGORY,"columnPassword").getString());
    }

    public Map<String, String> getColumns() {
        return this.columns;
    }

    public String getUser() { return this.user; }

    public String getDatabase() { return this.database; }

    public String getPassword() { return this.password; }

    public String getHost() { return this.host; }

    public String getDialect() { return this.dialect; }

    public String getTable() { return this.table; }

    public int getPort() { return this.port; }

}

