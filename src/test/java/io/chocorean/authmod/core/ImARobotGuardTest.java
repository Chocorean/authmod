package io.chocorean.authmod.core;

import io.chocorean.authmod.core.exception.AuthmodError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImARobotGuardTest {

  private GuardInterface guard;

  @BeforeEach
  void init() {
    this.guard = new ImARobotGuard();
  }

  @Test
  public void testAuthenticate() throws AuthmodError {
    assertTrue(this.guard.authenticate(new Payload(null, new String[]{"I'm", "not", "a", "robot"})));
  }

  @Test
  public void testAuthenticateNullArgs() {
    assertThrows(Exception.class, () -> this.guard.authenticate(null));
  }

  @Test
  public void testRegister() throws AuthmodError {
    assertTrue(this.guard.register(null));
  }
  
  @Test
  public void testUpdate() throws AuthmodError {
    assertTrue(this.guard.update(null, null));
  }

}
