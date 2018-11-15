package io.chocorean.authmod.authentication.db;

import java.sql.Connection;

public interface IConnectionFactory {

    /**
     * Get a connection to database
     * @return Connection object
     */
    Connection getConnection();

}
