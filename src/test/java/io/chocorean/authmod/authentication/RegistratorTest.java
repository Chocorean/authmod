package io.chocorean.authmod.authentication;

import io.chocorean.authmod.authentication.datasource.DatabaseSourceStrategy;
import io.chocorean.authmod.authentication.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class RegistratorTest {

    private Registrator registrator;
    private RegistrationPayload payload;

    @BeforeEach
    void init() {
        this.payload = new RegistrationPayload();
        payload.setEmail("test@test.test");
        payload.setUsername("mcdostone");
        payload.setPasswordConfirmation("root");
        payload.setPassword("root");
        this.registrator = new Registrator();
    }

    @Test
    public void testDefaultConstructor() {
        Registrator registrator = new Registrator();
        assertTrue(registrator.getDataSourceStrategy().getClass().equals(FileDataSourceStrategy.class), "Default data source strategy should be FileDataSourceStrategy");
    }

    @Test
    public void testConstructor() throws SQLException {
        Registrator registrator = new Registrator(new DatabaseSourceStrategy(null));
        assertTrue(registrator.getDataSourceStrategy().getClass().equals(DatabaseSourceStrategy.class), "Data source strategy should be DatatabaseSourceStrategy");
    }

    @Test
    public void testRegister() throws RegistrationException {
        boolean registered = this.registrator.register(this.payload);
        assertTrue(registered, "Player should be logged");
    }

    @Test
    public void testRegisterInvalidEmail() {
        assertThrows(InvalidEmailException.class, () -> {
            this.registrator.register(this.payload.setEmail("wrong"));
        });
    }

    @Test
    public void testRegisterPlayerAlreadyExist() {
        assertThrows(PlayerAlreadyExistException.class, () -> {
            this.registrator.register(this.payload.setEmail("root@root.root"));
        });
    }

    @Test
    public void testRegisterUnauthorizedHostedDomain() {
        assertThrows(UnauthorizedHostedDomainException.class, () -> {
            this.registrator.register(this.payload.setEmail("root@root.fr"));
        });
    }

    @Test
    public void testLoginNullParams() throws RegistrationException {
        boolean registered = this.registrator.register(null);
        assertFalse(registered, "Can't register the player, no payload provided");
    }

}
