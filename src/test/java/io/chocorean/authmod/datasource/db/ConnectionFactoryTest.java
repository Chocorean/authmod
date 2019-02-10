package io.chocorean.authmod.datasource.db;

import io.chocorean.authmod.DBHelpers;
import io.chocorean.authmod.guard.datasource.db.ConnectionFactory;
import io.chocorean.authmod.guard.datasource.db.IConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionFactoryTest {

    @Test
    public void testConstructorUrl() throws SQLException {
        IConnectionFactory connectionFactory = DBHelpers.getConnectionFactory();
        Connection connection = connectionFactory.getConnection();
        assertNotNull(connection, "Connection should be configured correctly");
    }

    @Test
    public void testConstructorParamsSqlite() throws SQLException {
        DBHelpers.initDatabaseFile();
        IConnectionFactory connectionFactory = DBHelpers.getConnectionFactory(
            "sqlite",
            null,
            0,
            DBHelpers.dataFile.getAbsolutePath(),
            null,
            null
        );
        Connection connection = connectionFactory.getConnection();
        assertNotNull(connection, "Connection should be configured correctly");
        assertEquals(
            "jdbc:sqlite:" + DBHelpers.dataFile.getAbsolutePath(),
            connection.getMetaData().getURL(),
            "JDBC URL is malformed for SQLite"
        );
    }

    @Test
    public void testConstructorParamsMariadb() {
        IConnectionFactory connectionFactory = new ConnectionFactory(
            "mariadb",
            "localhost",
            3306,
            "minecraft",
            null,
            null
        );
        assertEquals(
            "jdbc:mariadb://localhost:3306/minecraft",
            connectionFactory.getURL(),
            "JDBC URL is malformed for mariadb"
        );
    }

    @Test
    public void testGetConnectionSQLError() {
        IConnectionFactory connectionFactory = new ConnectionFactory(
            "mariadb",
            "localhost",
            3306,
            "minecraft",
            null,
            null
        );
        assertThrows(SQLException.class, connectionFactory::getConnection);
    }

}

