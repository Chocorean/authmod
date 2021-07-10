package io.chocorean.authmod.command;

import static org.junit.jupiter.api.Assertions.*;

import io.chocorean.authmod.core.DataSourceGuard;
import io.chocorean.authmod.core.Payload;
import io.chocorean.authmod.core.PayloadInterface;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChangePasswordCommandTest extends CommandTest {

  @BeforeEach
  void init() throws Exception {
    super.initProperties("changepassword");
    this.command = new ChangePasswordCommand(this.handler, this.guard);
    this.guard.register(new Payload(this.player, new String[] { this.password, this.password }));
  }

  @Test
  void testExecute() {
    this.handler.authorizePlayer(this.playerEntity);
    int res = ChangePasswordCommand.execute(this.source, this.handler, this.guard, this.createPayload("baguette"));
    assertEquals(0, res);
  }

  @Test
  void testExecuteIdentifierRequired() throws Exception {
    File file = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
    Files.deleteIfExists(file.toPath());
    this.guard = new DataSourceGuard(this.dataSource, true);
    this.guard.register(new Payload(this.player, new String[] { "Bernard", this.password, this.password }));
    this.handler.authorizePlayer(this.playerEntity);
    int res = ChangePasswordCommand.execute(this.source, this.handler, this.guard, this.createPayload("baguette"));
    assertEquals(0, res);
  }

  @Test
  void testWrongOldPassword() {
    this.handler.authorizePlayer(this.playerEntity);
    PayloadInterface payload = new Payload(
      this.player,
      new String[] { "pain au chocolat", "chausson au pommes", "chausson au pommes" }
    );
    int res = ChangePasswordCommand.execute(this.source, this.handler, this.guard, payload);
    assertNotEquals(0, res);
  }

  @Test
  void testSamePassword() {
    int res = ChangePasswordCommand.execute(this.source, this.handler, this.guard, this.createPayload(this.password));
    assertNotEquals(0, res);
  }

  @Test
  void testNotLogged() {
    int res = ChangePasswordCommand.execute(this.source, this.handler, this.guard, this.createPayload("opera"));
    assertNotEquals(0, res);
    assertFalse(this.handler.isLogged(this.playerEntity));
  }

  private PayloadInterface createPayload(String newPassword) {
    return new Payload(this.player, new String[] { this.password, newPassword, newPassword });
  }
}
