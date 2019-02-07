package io.chocorean.authmod.authentication;

import io.chocorean.authmod.authentication.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.authentication.datasource.IDataSourceStrategy;
import io.chocorean.authmod.exception.*;
import io.chocorean.authmod.model.IPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class RegistratorTest {

    private Registrator registrator;
    private RegistrationPayload payload;
    private IDataSourceStrategy dataSource;

    @BeforeEach
    void init() throws IOException {
        File dataFile = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
        if(dataFile.exists()) {
            dataFile.delete();
        }
        this.dataSource = new FileDataSourceStrategy(dataFile);
        dataFile.createNewFile();
        this.payload = new RegistrationPayload();
        payload.setEmail("test@test.test");
        payload.setUsername("mcdostone");
        payload.setPassword("rootroot");
        payload.setPasswordConfirmation("rootroot");
        this.registrator = new Registrator(this.dataSource);
    }

    @Test
    public void testDefaultConstructor() {
        Registrator registrator = new Registrator();
        assertEquals(registrator.getDataSourceStrategy().getClass(), FileDataSourceStrategy.class, "Default data source strategy should be FileDataSourceStrategy");
    }

    @Test
    public void testRegister() throws AuthmodException {
        boolean registered = this.registrator.register(this.payload);
        assertTrue(registered, "Player should be logged");
    }

    @Test
    public void testHashedPassword() throws AuthmodException {
        this.registrator.register(this.payload);
        IPlayer player = this.dataSource.find(this.payload.getEmail(), null);
        assertNotEquals(player.getPassword(), this.payload.getPassword(), "Passwords should be hashed");
    }

    @Test
    public void testRegisterInvalidEmail() {
        assertThrows(InvalidEmailException.class, () -> this.registrator.register(this.payload.setEmail("wrong")));
    }

    @Test
    public void testRegisterPlayerAlreadyExist() throws AuthmodException {
        this.registrator.register(this.payload);
        assertThrows(PlayerAlreadyExistException.class, () -> this.registrator.register(this.payload.setEmail("root@root.root")));
    }

    @Test
    public void testLoginNullParams() throws AuthmodException {
        boolean registered = this.registrator.register(null);
        assertFalse(registered, "Can't register the player, no payload provided");
    }

}
