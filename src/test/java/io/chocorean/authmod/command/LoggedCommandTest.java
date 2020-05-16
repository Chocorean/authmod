package io.chocorean.authmod.command;

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
  public void init() throws Exception {
    super.initProperties();
    this.registerPlayer();
  }

  @Test
  void testExecute() throws Exception {
    int res = LoggedCommand.execute(this.source, this.source.asPlayer(), this.handler);
    assertEquals(0, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteAlreadyLogged() throws Exception {
    handler.authorizePlayer(this.playerEntity);
    assertTrue(this.handler.isLogged(this.playerEntity));
    int res = LoggedCommand.execute(this.source, this.source.asPlayer(), this.handler);
    assertEquals(0, res);
    assertTrue(this.handler.isLogged(this.playerEntity));
  }

}
