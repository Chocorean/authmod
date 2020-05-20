package io.chocorean.authmod.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterWithIdentifierCommandTest extends CommandTest {

  @BeforeEach
  public void init() throws Exception {
    super.initProperties("register");
    this.payload = new Payload(this.player, new String[]{this.player.getUsername(), this.password});
    this.guard = new DataSourceGuard(this.dataSource, true);
    this.command = new RegisterWithIdentifierCommand(this.handler, this.guard);
    this.guard.register(new Payload(this.player, new String[]{"Jackson", this.password, this.password}));
  }

  @Test
  void testConstructor() {
    assertNotNull(this.command);
  }

  @Test
  void testGetCommandBuilder() {
    assertEquals(this.command.getCommandBuilder().getLiteral(), "register");
  }

  @Test
  void testRun() throws CommandSyntaxException {
    assertNotEquals(0, this.command.run(this.context));
  }

  @Test
  void testExecute() {
    this.payload = new Payload(this.player, new String[]{this.player.getUsername(), this.password});
    this.guard = new DataSourceGuard(this.dataSource, true);
    int res = LoginCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertNotEquals(0, res);
  }

  @Test
  void testExecuteWrongNumberOfArgs() {
    this.payload = new Payload(this.player, new String[]{"Heaven"});
    int res = LoginCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertNotEquals(0, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

}
