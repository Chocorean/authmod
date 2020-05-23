package io.chocorean.authmod.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.core.Payload;
import io.chocorean.authmod.util.text.ServerLanguageMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoggedCommandTest extends CommandTest {

  @BeforeAll
  static void setUp() {
    ServerLanguageMap.init();
  }

  @BeforeEach
  void init() throws Exception {
    super.initProperties("logged");
    this.command = new LoggedCommand(this.handler);
    this.guard.register(new Payload(this.player, new String[]{this.password, this.password}));
  }

  @Test
  void testExecute() throws Exception {
    int res = LoggedCommand.execute(this.source, this.source.asPlayer(), this.handler);
    assertEquals(0, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

  @Test
  @Override
  void testGetParameters() {
    assertNull(this.command.getParameters());
  }

  @Test
  void testExecuteAlreadyLogged() throws Exception {
    handler.authorizePlayer(this.playerEntity);
    assertTrue(this.handler.isLogged(this.playerEntity));
    int res = LoggedCommand.execute(this.source, this.source.asPlayer(), this.handler);
    assertEquals(0, res);
    assertTrue(this.handler.isLogged(this.playerEntity));
  }

  @Test
  @Override
  void testRun() throws CommandSyntaxException {
    assertEquals(0, this.command.run(this.context));
  }

}
