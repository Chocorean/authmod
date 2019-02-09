package io.chocorean.authmod.proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CommonProxyTest {

    private CommonProxy commonProxyProxy;

    @BeforeEach
    void init() {
        this.commonProxyProxy = new CommonProxy();
    }

    @Test
    public void testRegisterRenderers() {
        assertDoesNotThrow(() -> this.commonProxyProxy.registerRenderers());
    }

}
