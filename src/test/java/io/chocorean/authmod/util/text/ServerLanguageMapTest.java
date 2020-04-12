package io.chocorean.authmod.util.text;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ServerLanguageMapTest {

  private final String key = "authmod.welcome";

  @BeforeEach
  public void setup() throws NoSuchFieldException, IllegalAccessException {
    Field instance = ServerLanguageMap.class.getDeclaredField("INSTANCE");
    instance.setAccessible(true);
    instance.set(null, null);
  }

  @Test
  public void testConstructor() {
    ServerLanguageMap.init();
    assertNotNull(ServerLanguageMap.getInstance().translateKey(key));
  }

  @Test
  public void testConstructorEnLang() {
    ServerLanguageMap.init("en_us");
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
  public void testReplaceAllNoInstance() {
    String welcome = "Bonsoir Elliot!";
    Map<String, String> newTranslations = new HashMap<>();
    newTranslations.put(key, welcome);
    ServerLanguageMap.replaceWith(newTranslations);
    assertNotEquals(welcome, ServerLanguageMap.getInstance().translateKey(key));
  }

  @Test
  public void testGetInstance() {
    assertNotNull(ServerLanguageMap.getInstance());
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
