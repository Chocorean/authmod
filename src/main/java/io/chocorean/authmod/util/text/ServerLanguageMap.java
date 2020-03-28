package io.chocorean.authmod.util.text;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.chocorean.authmod.AuthMod;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class ServerLanguageMap {

   private static final Logger LOGGER = LogManager.getLogger();
   private static final Pattern NUMERIC_VARIABLE_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
   private static ServerLanguageMap INSTANCE;
   private final Map<String, String> languageList = Maps.newHashMap();
   private long lastUpdateTimeInMilliseconds;
   private final static String DEFAULT_LANG = "en_us";

   private ServerLanguageMap(String language) {
     this.loadLangFile(DEFAULT_LANG);
     if(language != null && !language.equals(DEFAULT_LANG)) {
       this.loadLangFile(language);
     }
   }

   private void loadLangFile(String language) {
     String path = "/assets/authmod/lang/"+ language.trim() +".json";
     try (InputStream inputstream = ServerLanguageMap.class.getResourceAsStream(path)) {
       JsonElement jsonelement = (new Gson()).fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), JsonElement.class);
       JsonObject jsonobject = JSONUtils.getJsonObject(jsonelement, "strings");
       for(Entry<String, JsonElement> entry : jsonobject.entrySet()) {
         String s = NUMERIC_VARIABLE_PATTERN.matcher(JSONUtils.getString(entry.getValue(), entry.getKey())).replaceAll("%$1s");
         this.languageList.put(entry.getKey(), s);
       }
       this.lastUpdateTimeInMilliseconds = Util.milliTime();
     } catch (Exception exception) {
       LOGGER.error("Couldn't read strings from " + path, exception);
     }
   }

  public static void init() {
    init(null);
  }

   public static void init(String input) {
     if(INSTANCE == null) {
      INSTANCE = new ServerLanguageMap(input);
     }
   }

  public static synchronized void replaceWith(Map<String, String> dict) {
     if(INSTANCE != null) {
       INSTANCE.languageList.putAll(dict);
       INSTANCE.lastUpdateTimeInMilliseconds = Util.milliTime();
     }
  }

   public static ServerLanguageMap getInstance() {
     if(INSTANCE == null)
       init();
      return INSTANCE;
   }

   public synchronized String translateKey(String key) {
      return this.tryTranslateKey(key);
   }


   private String tryTranslateKey(String key) {
      String s = this.languageList.get(key);
      s = s == null ? this.languageList.get(AuthMod.MODID.concat(".").concat(key)) : s;
      return s == null ? key : s;
   }

   public synchronized boolean exists(String key) {
      return this.languageList.containsKey(key);
   }

   public long getLastUpdateTimeInMilliseconds() {
      return this.lastUpdateTimeInMilliseconds;
   }
}
