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

public class AuthenticatorTest {

    private Authenticator authenticator;
    private Registrator registrator;
    private LoginPayload payload;
    private RegistrationPayload registrationPayload;
    private File dataFile;
    private IDataSourceStrategy dataSource;
    private IPlayer player;

    @BeforeEach
    void init() throws IOException, AuthmodException {
        this.dataFile = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
        if(this.dataFile.exists()) {
            this.dataFile.delete();
        }
        this.dataSource = new FileDataSourceStrategy(this.dataFile);
        this.dataFile.createNewFile();
        this.payload = new LoginPayload();
        this.registrationPayload = new RegistrationPayload();
        payload.setEmail("test@test.test");
        payload.setUsername("mcdostone");
        payload.setPassword("rootroot");
        registrationPayload.setEmail(payload.getEmail());
        registrationPayload.setUsername(payload.getUsername());
        registrationPayload.setPasswordConfirmation(payload.getPassword());
        registrationPayload.setPassword(payload.getPassword());
        this.authenticator = new Authenticator(this.dataSource);
        this.registrator = new Registrator(this.dataSource);
        this.player = PlayerFactory.createFromLoginPayload(payload);
        this.registerPlayer();
    }

    private boolean registerPlayer() throws AuthmodException {
        return this.registrator.register(this.registrationPayload);
    }

    @Test
    public void testConstructor() {
        Authenticator authenticator = new Authenticator(new FileDataSourceStrategy(this.dataFile));
        assertEquals(authenticator.getDataSourceStrategy().getClass(), FileDataSourceStrategy.class, "Data source strategy should be FileDataSourceStrategy");
    }

    @Test
    public void testLogin() throws LoginException {
        boolean logged = this.authenticator.login(this.payload);
        assertTrue(logged, "Player should be logged");
    }

    @Test
    public void testLoginWrongPassword() throws LoginException {
        boolean logged = this.authenticator.login(this.payload.setPassword("wrong"));
        assertFalse(logged, "Player should not be logged");
    }

    @Test
    public void testLoginUnknownPlayer() {
        assertThrows(PlayerNotFoundException.class, () -> this.authenticator.login(this.payload.setEmail("freddie.wong@rocketjump.com")));
    }

    @Test
    public void testLoginDifferentUsername() {
        assertThrows(DifferentUsernameException.class, () -> this.authenticator.login(this.payload.setUsername("wrong")));
    }

    @Test
    public void testLoginBanned() throws RegistrationException {
        this.player.setBanned(true);
        this.player.setUsername("banner");
        this.player.setEmail(null);
        this.dataSource.add(player);
        LoginPayload p = new LoginPayload();
        p.setUsername("banner");
        p.setPassword("korben");
        assertThrows(BannedPlayerException.class, () -> this.authenticator.login(p));
    }

    @Test
    public void testLoginNullParams() throws LoginException {
        boolean logged = this.authenticator.login(null);
        assertFalse(logged, "Can't be logged, no payload provided");
    }

}
