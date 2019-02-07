package io.chocorean.authmod.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPayloadTest {

    private LoginPayload payload;

    @BeforeEach
    void init() {
        String username = "mcdostone";
        String email = "mcdostone@gmail.com";
        String uuid = "7128022b-9195-490d-9bc8-9b42ebe2a8e3";
        String password = "korben";
        this.payload = new LoginPayload();
        this.payload.setPassword(password);
        this.payload.setUsername(username);
        this.payload.setEmail(email);
        this.payload.setUuid(uuid);
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

}
