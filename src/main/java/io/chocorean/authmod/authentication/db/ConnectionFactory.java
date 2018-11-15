package io.chocorean.authmod.authentication.db;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.config.AuthModDatabaseConfig;

import java.sql.*;

public class ConnectionFactory implements IConnectionFactory{

    private final AuthModDatabaseConfig config;

    public ConnectionFactory(AuthModDatabaseConfig config) {
        this.config = config;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(String.format("jdbc:%s://%s:%s/%s",
                    this.config.getDialect(),
                    this.config.getHost(),
                    this.config.getPort(),
                    this.config.getDatabase()),
                    this.config.getUser(),
                    this.config.getPassword());
        } catch (SQLException ex) {
            throw new RuntimeException(AuthMod.getConfig().getDatabaseErrorMsg(), ex);
        }
    }

}
