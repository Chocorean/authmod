package io.chocorean.authmod.db;

import io.chocorean.authmod.AuthMod;

import java.sql.*;

/**
 * Connect to Database
 * @author hany.said
 */
public class ConnectionFactory {
    /**
     * Get a connection to database
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(String.format("jdbc:%s://%s/%s",
                    AuthMod.config.getDatabaseConfig().getDialect(),
                    AuthMod.config.getDatabaseConfig().getHost(),
                    AuthMod.config.getDatabaseConfig().getName()),
                    AuthMod.config.getDatabaseConfig().getUser(),
                    AuthMod.config.getDatabaseConfig().getPassword());
        } catch (SQLException ex) {
            throw new RuntimeException("Error connecting to the database", ex);
        }
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        IPlayersDAO dao = new PlayersDAO(connection);
        System.out.println(dao.findAll());
    }
}