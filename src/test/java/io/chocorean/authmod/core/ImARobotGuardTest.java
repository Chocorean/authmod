package io.chocorean.authmod.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImARobotGuardTest {

  private GuardInterface guard;

  @BeforeEach
  void init() {
    this.guard = new ImARobotGuard();
  }

  @Test
  public void testAuthenticate() {
    assertTrue(this.guard.authenticate(new Payload(null, new String[]{"I'm", "not", "a", "robot"})));
  }

  @Test
  public void testAuthenticateNullArgs() {
    assertFalse(this.guard.authenticate(null));
  }

  @Test
  public void testRegister() {
    assertTrue(this.guard.register(null));
  }

}
