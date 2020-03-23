package io.chocorean.authmod.core;

import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.datasource.DatabaseStrategy;
import io.chocorean.authmod.core.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.core.datasource.db.ConnectionFactoryInterface;
import io.chocorean.authmod.core.datasource.db.DBHelpers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class DataSourceGuardTest {

  private GuardInterface guard;
  private String password = "my-very-l0ng-password";
  private final static File FILE = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
  private static ConnectionFactoryInterface connectionFactory;
  private PlayerInterface player;
  private DataSourceStrategyInterface dataSourceStrategy;
  private PayloadInterface registrationPayload;
  private PayloadInterface loginPayload;

  void init(DataSourceStrategyInterface impl) throws Exception {
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

  @ParameterizedTest
  @MethodSource("parameters")
  void testAuthenticateWrongPassword(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    this.guard.register(registrationPayload);
    assertTrue(this.guard.authenticate(loginPayload));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void testAuthenticateUnknownPlayer(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    PayloadInterface payload = new Payload(this.player.setUsername("admin"), new String[]{"rootroot"});
    assertFalse(this.guard.authenticate(payload));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void testAuthenticateBanned(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    this.guard.register(registrationPayload);
    if(impl instanceof DatabaseStrategy) {
      DBHelpers.banPlayer(connectionFactory, registrationPayload.getPlayer().getUsername());
    }
    this.dataSourceStrategy.find(registrationPayload.getPlayer().getUsername()).setBanned(true);
    assertFalse(this.guard.authenticate(this.loginPayload));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void testAuthenticatePayloadNull(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    assertFalse(this.guard.authenticate(null));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void testRegisterPayloadNull(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    assertFalse(this.guard.register(null));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void testGetError(DataSourceStrategyInterface impl) throws Exception {
    init(impl);
    assertNotNull(this.guard.getError());
  }

  static Stream<Arguments> parameters() throws Exception {
    connectionFactory = DBHelpers.initDatabase();
    return Stream.of(
      Arguments.of(new FileDataSourceStrategy(FILE)),
      Arguments.of(new DatabaseStrategy(connectionFactory))
    );
  }

}
