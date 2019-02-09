package io.chocorean.authmod.guard.datasource.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionFactory {

    /**
     * Get a connection to database
     * @return Connection object
     */
    Connection getConnection() throws SQLException;

    String getURL();

}
