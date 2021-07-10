package io.chocorean.authmod.util.text;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ServerTranslationTextComponentTest {

  @Test
  public void testConstructor() {
    ServerTranslationTextComponent component = new ServerTranslationTextComponent("authmod.logged.yes");
    assertEquals(component.getText(), "yes");
  }
}
