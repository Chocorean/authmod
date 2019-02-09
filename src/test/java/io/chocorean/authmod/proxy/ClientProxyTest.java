package io.chocorean.authmod.proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ClientProxyTest {

    private ClientProxy clientProxy;

    @BeforeEach
    void init() {
        this.clientProxy = new ClientProxy();
    }

    @Test
    public void testRegisterRenderers() {
        assertDoesNotThrow(() -> this.clientProxy.registerRenderers());
    }

}
