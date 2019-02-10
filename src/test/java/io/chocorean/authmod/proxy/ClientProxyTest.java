package io.chocorean.authmod.proxy;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientProxyTest {
  private ClientProxy clientProxy;

  @BeforeEach
  void init() {
    this.clientProxy = new ClientProxy();
  }

  @Test
  void testRegisterRenderers() {
    assertDoesNotThrow(() -> this.clientProxy.registerRenderers());
  }
}
