package io.chocorean.authmod.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

  private final String uuid = "7128022b-9195-490d-9bc8-9b42ebe2a8e3";
  private PlayerInterface player;

  @BeforeEach
  void init() {
    this.player = new Player(null, uuid);
  }

  @Test
  void testSetUuidShortFormat() {
    this.player.setUuid(this.uuid.replaceAll("-", ""));
    assertEquals(this.uuid, this.player.getUuid(), "The UUID format should be X-X-X-X-X");
  }

  @Test
  void testSetUuidLongFormat() {
    this.player.setUuid(this.uuid);
    assertEquals(this.uuid, this.player.getUuid(), "The UUID format should be X-X-X-X-X");
  }

  @Test
  void testSetUuidIncorrect() {
    this.player.setUuid("15");
    assertEquals(0, this.player.getUuid().length());
  }

  @Test
  void testIsPremium() {
    this.player.setUuid(null);
    assertFalse(this.player.isPremium(), "The player should not be premium");
  }

  @Test
  void testSetUuidNullParam() {
    this.player.setUuid(null);
    assertEquals(0, this.player.getUuid().length(), "The UUID should be empty");
  }

  @Test
  void testToString() {
    player.setUsername("korben");
    assertEquals("{username: " + this.player.getUsername() + ", uuid: " + this.player.getUuid() + "}", player.toString());
  }

}
