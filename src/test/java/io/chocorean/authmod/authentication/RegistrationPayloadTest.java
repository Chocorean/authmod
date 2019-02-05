package io.chocorean.authmod.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationPayloadTest {

    private String username;
    private String email;
    private String uuid;
    private String passwordConfirmation;
    private String password;
    private RegistrationPayload payload;

    @BeforeEach
    void init() {
        this.username = "mcdostone";
        this.email = "mcdostone@gmail.com";
        this.uuid = "7128022b-9195-490d-9bc8-9b42ebe2a8e3";
        this.password = "korben";
        this.passwordConfirmation = "korben";
        this.payload = new RegistrationPayload();
        this.payload.setPasswordConfirmation(this.passwordConfirmation);
        this.payload.setPassword(this.password);
        this.payload.setUsername(this.username);
        this.payload.setEmail(this.email);
        this.payload.setUuid(this.uuid);
    }

    @Test
    public void testIsValid() {
        assertTrue(this.payload.isValid(), "Payload should be valid");
    }

    @Test
    public void testIsValidShortPassword() {
        assertFalse(this.payload.setPassword("u").isValid(), "Payload should not be valid, password is too short");
    }

    @Test
    public void testIsValidIncorrectUuid() {
        assertFalse(this.payload.setUuid("uuid").isValid(), "payload should not be valid because of UUID");
    }

    @Test
    public void testIsValidNullUuid() {
        assertTrue(this.payload.setUuid(null).isValid(), "payload should be valid");
    }

    @Test
    public void testIsValidPasswordsAreDifferent() {
        this.payload.setPasswordConfirmation("root");
        assertFalse(this.payload.isValid(), "Payload should not be valid because of password confirmation");
    }

}
