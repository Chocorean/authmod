package io.chocorean.authmod.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.chocorean.authmod.core.DataSourceGuard;
import io.chocorean.authmod.core.Payload;
import io.chocorean.authmod.core.datasource.DataSourcePlayer;
import io.chocorean.authmod.core.datasource.FileDataSourceStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginCommandTest extends CommandTest {

  @BeforeEach
  public void init() throws Exception {
    super.initProperties("login");
    this.command = new LoginCommand(this.handler, this.guard);
    this.guard.register(new Payload(this.player, new String[]{this.password, this.password}));
  }

  @Test
  void testConstructor() {
    assertNotNull(this.command);
  }

  @Test
  void testGetCommandBuilder() {
    assertEquals(this.command.getCommandBuilder().getLiteral(), "login");
  }

  @Test
  void testRun() throws CommandSyntaxException {
    assertNotEquals(0, this.command.run(this.context));
  }

  @Test
  void testExecute() {
    int res = LoginCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertEquals(0, res);
    assertTrue(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteWrongPassword() {
    this.payload.getArgs()[0] = "wrongpass";
    int res = LoginCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertNotEquals(0, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteWrongNumberOfArgs() {
    this.payload = new Payload(this.player, new String[]{"rootrootme", "please"});
    int res = LoginCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertNotEquals(0, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteBanned() {
    FileDataSourceStrategy mock = mock(FileDataSourceStrategy.class);
    when(mock.find(player.getUsername())).thenReturn(new DataSourcePlayer(player).setBanned(true));
    int res = LoginCommand.execute(this.source, this.handler, new DataSourceGuard(mock), this.payload);
    assertEquals(1, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecutePlayerNotFound() {
    this.payload.getPlayer().setUsername("not_exist");
    int res = LoginCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertEquals(1, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

  @Test
  void testExecuteAlreadyLogged() {
    handler.authorizePlayer(playerEntity);
    assertTrue(this.handler.isLogged(this.playerEntity));
    int res = LoginCommand.execute(this.source, this.handler, this.guard, this.payload);
    assertEquals(0, res);
    assertTrue(this.handler.isLogged(this.playerEntity));
  }

}
