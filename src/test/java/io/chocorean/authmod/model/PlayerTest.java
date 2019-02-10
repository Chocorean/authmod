package io.chocorean.authmod.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {
  private Player player;
  private String uuid = "7128022b-9195-490d-9bc8-9b42ebe2a8e3";

  @BeforeEach
  void init() {
    this.player = new Player();
  }

  @Test
  public void testSetEmail() {
    String email = "root@root.fr";
    this.player.setEmail(email + ' ');
    assertEquals(
        email,
        this.player.getEmail(),
        "The email should contain whitespace character at the beginning or the end");
  }

  @Test
  public void testSetUsername() {
    String username = "mcdostone";
    this.player.setUsername(username + ' ');
    assertEquals(
        username,
        this.player.getUsername(),
        "The username should contain whitespace character at the beginning or the end");
  }

  @Test
  public void testSetUuidNullParam() {
    this.player.setUuid(null);
    assertEquals(this.player.getUuid().length(), 0, "The UUID should be empty");
  }

  @Test
  public void testSetEmailNullParam() {
    this.player.setEmail(null);
    assertEquals(this.player.getEmail().length(), 0, "The email should be null");
  }

  @Test
  public void testSetUuidShortFormat() {
    this.player.setUuid(this.uuid.replaceAll("-", ""));
    assertEquals(this.uuid, this.player.getUuid(), "The UUID format should be X-X-X-X-X");
  }

  @Test
  public void testSetUuidLongFormat() {
    this.player.setUuid(this.uuid);
    assertEquals(this.uuid, this.player.getUuid(), "The UUID format should be X-X-X-X-X");
  }

  @Test
  public void testSetUuidIncorrect() {
    this.player.setUuid("15");
    assertEquals(
        this.player.getUuid().length(), 0, "The UUID should be empty because it is incorrect");
  }

  @Test
  public void testIsPremium() {
    this.player.setUuid(null);
    assertFalse(this.player.isPremium(), "The player should not be premium");
  }

  @Test
  public void testToString() {
    player.setUsername("korben");
    player.setEmail("korben.dallas@gmail.com");
    assertEquals(
        "{korben.dallas@gmail.com, korben}",
        player.toString(),
        "Player is described by an optional email and a username");
  }
}
