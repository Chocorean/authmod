package io.chocorean.authmod.authentication.db;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.config.AuthModDatabaseConfig;

import java.sql.*;

public class ConnectionFactory implements IConnectionFactory{

    private String url;
    private String user;
    private String password;

    public ConnectionFactory(String url) {
        this(url, null, null);
    }

    public ConnectionFactory(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public ConnectionFactory(String dialect, String host, int port, String database, String user, String password) {
        this(String.format("jdbc:%s://%s:%d/%s", dialect, host, port, database), user, password);
    }

    public Connection getConnection() {
        try {
            return user == null ? DriverManager.getConnection(this.url)  : DriverManager.getConnection(this.url, this.user, this.password);
        } catch (SQLException ex) {
            throw new RuntimeException(AuthMod.getConfig().getDatabaseErrorMsg(), ex);
        }
    }

}
