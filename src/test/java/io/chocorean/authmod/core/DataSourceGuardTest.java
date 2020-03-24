package io.chocorean.authmod.core;

import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.datasource.DatabaseStrategy;
import io.chocorean.authmod.core.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.core.datasource.db.ConnectionFactoryInterface;
import io.chocorean.authmod.core.datasource.db.DBHelpers;
import io.chocorean.authmod.core.exception.BannedPlayerError;
import io.chocorean.authmod.core.exception.PlayerAlreadyExistError;
import io.chocorean.authmod.core.exception.PlayerNotFoundError;
import io.chocorean.authmod.core.exception.WrongPasswordConfirmationError;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class DataSourceGuardTest {

  private GuardInterface guard;
  private final String password = "my-very-l0ng-password";
  private final static File FILE = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
  private static ConnectionFactoryInterface connectionFactory;
  private PlayerInterface player;
  private DataSourceStrategyInterface dataSourceStrategy;
  private PayloadInterface registrationPayload;
  private PayloadInterface loginPayload;

  private void init(DataSourceStrategyInterface impl) throws Exception {
    this.dataSourceStrategy = impl;
    if(impl instanceof FileDataSourceStrategy && FILE.exists()) {
      FILE.delete();
    }
    if(impl instanceof DatabaseStrategy) {
      DBHelpers.initDatabase();
    }
    this.guard = new DataSourceGuard(dataSourceStrategy);
    this.player = new Player("fsociety", null);
    this.registrationPayload = new Payload(this.player, new String[]{password, password});
    this.loginPayload = new Payload(this.player, new String[]{password});
  }

  @ParameterizedTest(name = "with {0}")
  @MethodSource("parameters")
  public void testRegister(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    assertTrue(this.guard.register(this.registrationPayload));
  }

  @ParameterizedTest(name = "with {0}")
  @MethodSource("parameters")
  public void testAuthenticate(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    this.guard.register(registrationPayload);
    PayloadInterface payload = new Payload(this.player, new String[]{"rootroot"});
    assertFalse(this.guard.authenticate(payload));
  }

  @ParameterizedTest(name = "with {0}")
  @MethodSource("parameters")
  void testAuthenticateWrongPassword(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    this.guard.register(registrationPayload);
    this.loginPayload = new Payload(this.player, new String[]{"qwertyqwerty"});
    assertFalse(this.guard.authenticate(loginPayload));
  }

  @ParameterizedTest(name = "with {0}")
  @MethodSource("parameters")
  void testAuthenticateUnknownPlayer(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    PayloadInterface payload = new Payload(this.player.setUsername("admin"), new String[]{"rootroot"});
    assertThrows(PlayerNotFoundError.class, () -> this.guard.authenticate(payload));
  }

  @ParameterizedTest(name = "with {0}")
  @MethodSource("parameters")
  void testAuthenticateBanned(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    this.guard.register(registrationPayload);
    if(impl instanceof DatabaseStrategy) {
      DBHelpers.banPlayer(connectionFactory, registrationPayload.getPlayer().getUsername());
    }
    this.dataSourceStrategy.find(registrationPayload.getPlayer().getUsername()).setBanned(true);
    assertThrows(BannedPlayerError.class, () -> this.guard.authenticate(this.loginPayload));
  }

  @ParameterizedTest(name = "with {0}")
  @MethodSource("parameters")
  public void testAuthenticateNullParam(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    assertThrows(Exception.class, () -> this.guard.authenticate(null));
  }

  @ParameterizedTest(name = "with {0}")
  @MethodSource("parameters")
  public void testRegisterNullParam(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    assertThrows(Exception.class, () -> this.guard.authenticate(null));
  }

  @ParameterizedTest(name = "with {0}")
  @MethodSource("parameters")
  void testRegisterPlayerAlreadyExist(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    this.guard.register(this.registrationPayload);
    assertThrows(PlayerAlreadyExistError.class, () -> this.guard.register(this.registrationPayload));
  }

  @ParameterizedTest(name = "with {0}")
  @MethodSource("parameters")
  void testRegisterIdentifierRequired(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    this.guard = new DataSourceGuard(dataSourceStrategy, true);
    this.registrationPayload = new Payload(this.player, new String[]{"Crumb", this.password, this.password});
    assertTrue(this.guard.register(this.registrationPayload));
  }

  @ParameterizedTest(name = "with {0}")
  @MethodSource("parameters")
  void testIncorrectPasswordConfirmation(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    this.guard = new DataSourceGuard(dataSourceStrategy, true);
    this.registrationPayload = new Payload(this.player, new String[]{"Crumb", this.password, this.password + "typo"});
    assertThrows(WrongPasswordConfirmationError.class, () -> this.guard.register(this.registrationPayload));
  }

  static Stream<Arguments> parameters() throws Exception {
    connectionFactory = DBHelpers.initDatabase();
    return Stream.of(
      Arguments.of(new FileDataSourceStrategy(FILE)),
      Arguments.of(new DatabaseStrategy(connectionFactory))
    );
  }

}
