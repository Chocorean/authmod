package com.chocorean.authmod.db;

import com.chocorean.authmod.authentification.IPlayersDAO;
import com.chocorean.authmod.authentification.PlayersDAO;

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
        String user = System.getenv("MINECRAFT_DB_USER") != null ? System.getenv("MINECRAFT_DB_USER") : "root";
        String host = System.getenv("MINECRAFT_DB_HOST") != null ? System.getenv("MINECRAFT_DB_HOST") : "localhost";
        String database = System.getenv("MINECRAFT_DB_NAME") != null ? System.getenv("MINECRAFT_DB_NAME") : "root";
        String password = System.getenv("MINECRAFT_DB_PASSWORD") != null ? System.getenv("MINECRAFT_DB_PASSWORD") : "root";
        String dialect = System.getenv("MINECRAFT_DB_DIALECT") != null ? System.getenv("MINECRAFT_DB_DIALECT") : "mariadb";
        try {
            return DriverManager.getConnection(String.format("jdbc:%s://%s/%s", dialect, host,database), user, password);
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