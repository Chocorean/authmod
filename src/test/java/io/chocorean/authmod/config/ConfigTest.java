package io.chocorean.authmod.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ConfigTest {

    @Test
    void testLoadConfig() {
      assertDoesNotThrow(() -> Config.load());
    }

    @Test
    void testAuthmodEnabled() {
      Config.load();
      assertFalse(Config.authmodEnabled());
    }
}
