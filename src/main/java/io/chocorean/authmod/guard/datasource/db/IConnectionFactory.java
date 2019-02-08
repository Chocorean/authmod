package io.chocorean.authmod.guard.datasource.db;

import java.sql.Connection;

public interface IConnectionFactory {

    /**
     * Get a connection to database
     * @return Connection object
     */
    Connection getConnection();

}
