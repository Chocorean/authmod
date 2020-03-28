package io.chocorean.authmod.util.text;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ServerLanguageMapTest {

  private final String key = "authmod.welcome";

  @Test
  public void testConstructor() {
    ServerLanguageMap.init();
    assertNotNull(ServerLanguageMap.getInstance().translateKey(key));
  }

  @Test
  public void testConstructorOtherLang() {
    ServerLanguageMap.init("fr_fr");
    assertNotNull(ServerLanguageMap.getInstance().translateKey(key));
  }

  @Test
  public void testConstructorLangNotFound() {
    ServerLanguageMap.init("en_en");
    assertNotNull(ServerLanguageMap.getInstance().translateKey(key));
  }

  @Test
  public void testReplaceAll() {
    ServerLanguageMap.init();
    String welcome = "Bonsoir Eliot!";
    Map<String, String> newTranslations = new HashMap<>();
    newTranslations.put(key, welcome);
    ServerLanguageMap.replaceWith(newTranslations);
    assertEquals(welcome, ServerLanguageMap.getInstance().translateKey(key));
  }

  @Test
  public void testExist() {
    ServerLanguageMap.init();
    assertTrue(ServerLanguageMap.getInstance().exists(key));
  }

  @Test
  public void testGetLastUpdateTimeInMilliseconds() {
    ServerLanguageMap.init();
    assertNotEquals(0, ServerLanguageMap.getInstance().getLastUpdateTimeInMilliseconds());
  }

}
