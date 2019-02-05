package io.chocorean.authmod.authentication;

import io.chocorean.authmod.authentication.datasource.DatabaseSourceStrategy;
import io.chocorean.authmod.authentication.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticatorTest {

    private Authenticator authenticator;
    private LoginPayload payload;

    @BeforeEach
    void init() {
        this.payload = new LoginPayload();
        payload.setEmail("test@test.test");
        payload.setUsername("mcdostone");
        payload.setPassword("root");
        this.authenticator = new Authenticator();
    }

    @Test
    public void testDefaultConstructor() {
        Authenticator authenticator = new Authenticator();
        assertTrue(authenticator.getDataSourceStrategy().getClass().equals(FileDataSourceStrategy.class), "Default data source strategy should be FileDataSourceStrategy");
    }

    @Test
    public void testConstructor() throws SQLException {
        Authenticator authenticator = new Authenticator(new DatabaseSourceStrategy(null));
        assertTrue(authenticator.getDataSourceStrategy().getClass().equals(DatabaseSourceStrategy.class), "Data source strategy should be DatatabaseSourceStrategy");
    }

    @Test
    public void testLogin() throws LoginException {
        boolean logged = this.authenticator.login(this.payload);
        assertTrue(logged, "Player should be logged");
    }

    @Test
    public void testLoginWrongPassword() {
        assertThrows(WrongPasswordException.class, () -> {
            this.authenticator.login(this.payload.setPassword("wrong"));
        });
    }

    @Test
    public void testLoginUnknownPlayer() {
        assertThrows(PlayerNotFoundException.class, () -> {
            this.authenticator.login(this.payload.setEmail("wrong"));
        });
    }

    @Test
    public void testLoginDifferentUsername() {
        assertThrows(DifferentUsernameException.class, () -> {
            this.authenticator.login(this.payload.setUsername("wrong"));
        });
    }

    @Test
    public void testLoginBanned() {
        assertThrows(BannedPlayerException.class, () -> {
            this.authenticator.login(this.payload.setUsername("banned"));
        });
    }

    @Test
    public void testLoginNullParams() throws LoginException {
        this.authenticator.login(null);
        boolean logged = this.authenticator.login(this.payload);
        assertFalse(logged, "Can't be logged, no payload provided");
    }

}
