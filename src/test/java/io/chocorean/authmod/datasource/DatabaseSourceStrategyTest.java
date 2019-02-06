package io.chocorean.authmod.datasource;

import io.chocorean.authmod.PlayerFactory;
import io.chocorean.authmod.authentication.datasource.DatabaseSourceStrategy;
import io.chocorean.authmod.authentication.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.authentication.datasource.IDataSourceStrategy;
import io.chocorean.authmod.authentication.db.ConnectionFactory;
import io.chocorean.authmod.authentication.db.IConnectionFactory;
import io.chocorean.authmod.exception.AuthmodException;
import io.chocorean.authmod.exception.PlayerAlreadyExistException;
import io.chocorean.authmod.exception.RegistrationException;
import io.chocorean.authmod.model.IPlayer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseSourceStrategyTest {

    private File dataFile;
    private IPlayer player;
    private IDataSourceStrategy dataSource;
    private static IConnectionFactory connectionFactory;

    @BeforeAll
    static void setupConnection() throws SQLException {
        File dbFile = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.db").toFile();
        if(dbFile.exists()) {
            dbFile.delete();
        }
        connectionFactory = new ConnectionFactory("jdbc:sqlite:" + dbFile.getAbsolutePath());
        Connection connection = connectionFactory.getConnection();
        if(connection != null) {
            DatabaseMetaData meta = connection.getMetaData();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE players (" +
                    "    id integer PRIMARY KEY, \n" +
                    "    email varchar(255) NOT NULL, \n" +
                    "    password varchar(255), \n" +
                    "    uuid varchar(255), \n" +
                    "    username varchar(255) NOT NULL, \n" +
                    "    banned INTEGER DEFAULT 0, \n" +
                    "    UNIQUE (email),\n" +
                    "    UNIQUE (uuid), \n" +
                    "    UNIQUE (username)\n" +
                    ");");
            System.out.println("The driver name is " + meta.getDriverName());
        }
    }

    private boolean registerPlayer() throws SQLException, RegistrationException {
        this.dataSource = new DatabaseSourceStrategy(connectionFactory);
        this.player = PlayerFactory.create();
        return dataSource.add(this.player);
    }

    @Test
    public void testConstructor() throws SQLException {
        new DatabaseSourceStrategy(connectionFactory);
    }

    @Test
    public void testConstructorTableIsDifferent()  {
        assertThrows(SQLException.class, () -> new DatabaseSourceStrategy(connectionFactory));
    }

    @Test
    public void testAdd() throws AuthmodException, SQLException {
        boolean added = this.registerPlayer();
        assertTrue(added, "The player should be registered");
    }

    @Test
    public void testAddDoublon() throws AuthmodException, SQLException {
        boolean added = this.registerPlayer();
        assertTrue(added, "The player should be registered");
        assertThrows(PlayerAlreadyExistException.class, () -> {
            this.registerPlayer();
        });
    }

    @Test
    public void testFindByEmail() throws RegistrationException, SQLException {
        this.registerPlayer();
            assertNotNull(dataSource.find(this.player.getEmail(), null), "The player should exist and be found");
    }

    @Test
    public void testFindByUsername() throws RegistrationException, SQLException {
        this.registerPlayer();
        assertNotNull(dataSource.find(null, player.getUsername()), "The player should exist and be found");
    }

    @Test
    public void testFindNotExist() throws RegistrationException, SQLException {
        this.registerPlayer();
        assertNull(dataSource.find("test@test.fr", "test"), "The player should not exist");
    }

    @Test
    public void testFindByUsernameOrEmail() throws RegistrationException, SQLException {
        this.registerPlayer();
        assertNotNull(dataSource.find(player.getEmail(), player.getUsername()), "The player should exist and be found");
    }

    @Test
    public void testFindNullParams() throws AuthmodException, SQLException {
        this.registerPlayer();
        assertNull(dataSource.find(null, null), "It should return null");
    }

}
