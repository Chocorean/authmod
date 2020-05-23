package io.chocorean.authmod.core;

import io.chocorean.authmod.core.datasource.BcryptPasswordHash;
import io.chocorean.authmod.core.datasource.PasswordHashInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BcryptPasswordHashTest {

  private PasswordHashInterface passwordHash;
  private final String password = "my-very-l0ng-password";

  @BeforeEach
  void init() {
    this.passwordHash = new BcryptPasswordHash();
  }

  @Test
  void testHash() {
    String hashed = this.passwordHash.hash(this.password);
    assertNotNull(hashed);
  }

  @Test
  void testCheck() {
    String hashed = this.passwordHash.hash(this.password);
    assertNotNull(hashed);
    assertTrue(this.passwordHash.check(hashed, this.password));
  }

  @Test
  void testHashNullParam() {
    String hashed = this.passwordHash.hash(null);
    assertNotNull(hashed);
  }

  @Test
  void testCheckEmptyParam() {
    assertFalse(this.passwordHash.check("", ""));
  }

  @Test
  void testCheckNullParams() {
    assertFalse(this.passwordHash.check(null, null));
  }

  @Test
  void testCheckNullParam1() {
    assertFalse(this.passwordHash.check("hash", null));
  }

  @Test
  void testCheckNullParam2() {
    assertFalse(this.passwordHash.check(null, this.password));
  }

  @Test
  void testCheckNotHash() {
    assertFalse(this.passwordHash.check("pelegrino", this.password));
  }

}
