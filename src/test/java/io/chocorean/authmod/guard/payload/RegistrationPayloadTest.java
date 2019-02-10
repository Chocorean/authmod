package io.chocorean.authmod.guard.payload;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationPayloadTest {
  private RegistrationPayload payload;

  @BeforeEach
  void init() {
    String username = "mcdostone";
    String email = "mcdostone@gmail.com";
    String uuid = "7128022b-9195-490d-9bc8-9b42ebe2a8e3";
    String password = "korben";
    String passwordConfirmation = "korben";
    this.payload = new RegistrationPayload();
    this.payload.setPasswordConfirmation(passwordConfirmation);
    this.payload.setPassword(password);
    this.payload.setUsername(username);
    this.payload.setEmail(email);
    this.payload.setUuid(uuid);
  }

  @Test
  void testIsValid() {
    assertTrue(this.payload.isValid(), "Payload should be valid");
  }

  @Test
  void testIsValidEmailRequired() {
    this.payload.setEmailRequired(true);
    this.payload.setEmail(null);
    assertTrue(this.payload.isEmailRequired(), "payload must have email");
    assertFalse(this.payload.isValid(), "Payload should not be valid, email is missing");
  }

  @Test
  void testIsValidShortPassword() {
    assertFalse(
        this.payload.setPassword("u").isValid(),
        "Payload should not be valid, password is too short");
  }

  @Test
  void testIsValidIncorrectUuid() {
    assertFalse(
        this.payload.setUuid("uuid").isValid(), "payload should not be valid because of UUID");
  }

  @Test
  void testIsValidNullUuid() {
    assertTrue(this.payload.setUuid(null).isValid(), "payload should be valid");
  }

  @Test
  void testIsValidPasswordsAreDifferent() {
    this.payload.setPasswordConfirmation("root");
    assertFalse(
        this.payload.isValid(), "Payload should not be valid because of password confirmation");
  }
}
