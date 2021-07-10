package io.chocorean.authmod.command;

import io.chocorean.authmod.core.Payload;
import org.junit.jupiter.api.BeforeEach;

class LoginCommandTest extends CommandTest {

  @BeforeEach
  void setup() throws Exception {
    super.initProperties("login");
    this.command = new LoginCommand(this.handler, this.guard);
    this.guard.register(new Payload(this.player, new String[] { this.password, this.password }));
  }
}
