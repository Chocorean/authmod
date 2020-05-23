package io.chocorean.authmod.core.datasource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataSourcePlayerTest {

  private DataSourcePlayerInterface player;

  @BeforeEach
  void init() {
    this.player = new DataSourcePlayer();
    this.player.setUuid("7128022b-9195-490d-9bc8-9b42ebe2a8e3");
  }

  @Test
  void testSetIdentifier() {
    String email = "root@root.fr";
    this.player.setIdentifier(email + ' ');
    assertEquals(email, this.player.getIdentifier());
  }

  @Test
  void testSetUsername() {
    String username = "mcdostone";
    this.player.setUsername(username + ' ');
    assertEquals(username, this.player.getUsername());
  }

  @Test
  void testIsPremium() {
    assertTrue(this.player.isPremium());
  }

  @Test
  void testSetPassword() {
    String password = "rootroot ";
    this.player.setPassword(password);
    assertEquals(this.player.getPassword(), password);
  }

  @Test
  void testSetIdentifierNullParam() {
    this.player.setIdentifier(null);
    assertEquals(0, this.player.getIdentifier().length(), "The email should be null");
  }

  @Test
  void testToString() {
    assertNotNull(this.player.toString());
  }

}
