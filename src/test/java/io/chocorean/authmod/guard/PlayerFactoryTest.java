package io.chocorean.authmod.guard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.chocorean.authmod.guard.payload.RegistrationPayload;
import io.chocorean.authmod.model.IPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerFactoryTest {
  private String username;
  private String email;
  private String uuid;
  private String password;
  private RegistrationPayload payload;

  @BeforeEach
  void init() {
    this.username = "mcdostone";
    this.email = "mcdostone@gmail.com";
    this.uuid = "7128022b-9195-490d-9bc8-9b42ebe2a8e3";
    this.password = "korben";
    this.payload = new RegistrationPayload();
    this.payload.setPasswordConfirmation(this.password);
    this.payload.setPassword(this.password);
    this.payload.setUsername(this.username);
    this.payload.setEmail(this.email);
    this.payload.setUuid(this.uuid);
  }

  @Test
  public void testCreateFromRegistrationPayload() {
    IPlayer player = PlayerFactory.createFromRegistrationPayload(this.payload);
    assertEquals(this.username, player.getUsername(), "Username should not change");
    assertEquals(this.email, player.getEmail(), "Email should not change");
    assertEquals(this.uuid, player.getUuid(), "UUID should not change");
    assertEquals(this.password, player.getPassword(), "password should not change");
  }
}
