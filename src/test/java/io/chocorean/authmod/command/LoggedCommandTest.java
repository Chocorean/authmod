package io.chocorean.authmod.command;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.chocorean.authmod.core.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoggedCommandTest extends CommandTest {

  @BeforeEach
  void setup() throws Exception {
    super.initProperties("logged");
    this.command = new LoggedCommand(this.handler);
    this.guard.register(new Payload(this.player, new String[] { this.password, this.password }));
  }

  @Test
  void testExecuteAlreadyLogged() throws Exception {
    handler.authorizePlayer(this.playerEntity);
    assertTrue(this.handler.isLogged(this.playerEntity));
    this.command.execute(this.server, this.playerEntity, new String[] {});
    assertTrue(this.handler.isLogged(this.playerEntity));
  }
}
