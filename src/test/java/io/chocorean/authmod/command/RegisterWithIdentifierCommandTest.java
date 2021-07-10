package io.chocorean.authmod.command;

import static org.junit.jupiter.api.Assertions.*;

import io.chocorean.authmod.core.DataSourceGuard;
import io.chocorean.authmod.core.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterWithIdentifierCommandTest extends CommandTest {

  @BeforeEach
  void init() throws Exception {
    super.initProperties("register");
    this.payload = new Payload(this.player, new String[] { this.player.getUsername(), this.password });
    this.guard = new DataSourceGuard(this.dataSource, true);
    this.command = new RegisterWithIdentifierCommand(this.handler, this.guard);
    this.guard.register(new Payload(this.player, new String[] { "Jackson", this.password, this.password }));
  }

  @Test
  void testExecute() {
    this.payload = new Payload(this.player, new String[] { this.player.getUsername(), this.password });
    this.guard = new DataSourceGuard(this.dataSource, true);
    int res = LoginCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertNotEquals(0, res);
  }

  @Test
  void testExecuteWrongNumberOfArgs() {
    this.payload = new Payload(this.player, new String[] { "Heaven" });
    int res = LoginCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertNotEquals(0, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }
}
