package io.chocorean.authmod.config;

import io.chocorean.authmod.AuthMod;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class I18nConfig {

  @Config.Comment("authmod.error")
  public String error = "";

  @Config.Comment("authmod.welcome")
  public String welcome = "";

  @Config.Comment("authmod.wakeUp")
  public String wakeUp = "";

  @Config.Comment("authmod.banned")
  public String banned = "";

  @Config.Comment("authmod.wrongPassword")
  public String wrongPassword = "";

  @Config.Comment("authmod.wrongPasswordConfirmation")
  public String wrongPasswordConfirmation = "";

  @Config.Comment("authmod.logged.yes")
  public String yes = "";

  @Config.Comment("authmod.logged.no")
  public String no = "";

  @Config.Comment("authmod.logged.usage")
  public String loggedUsage = "";

  @Config.Comment("authmod.login.usage")
  public String loginUsage = "";

  @Config.Comment("authmod.login.notFound")
  public String loginNotFound = "";

  @Config.Comment("authmod.login.success")
  public String loginSuccess = "";

  @Config.Comment("authmod.login.wrongUUID")
  public String loginWrongUuid = "";

  @Config.Comment("authmod.login.wrongUsername")
  public String loginWrongUsername = "";

  @Config.Comment("authmod.register.exist")
  public String registerExist = "";

  @Config.Comment("authmod.register.usage")
  public String registerUsage = "";

  @Config.Comment("authmod.register.success")
  public String registerSuccess = "";

  @Config.Comment("authmod.changepassword.success")
  public String changePasswordSucccess = "";

  @Config.Comment("authmod.changepassword.same")
  public String changePasswordSame = "";

  @Config.Comment("authmod.changepassword.usage")
  public String changePasswordUsage = "";

  @SubscribeEvent
  public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals(AuthMod.MODID)) {
      ConfigManager.sync(AuthMod.MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
    }
  }

  public Map<String, String> getTranslations() {
    Map<String, String> translations = new HashMap<>();
    translations.put("authmod.error", error);
    translations.put("authmod.welcome", welcome);
    translations.put("authmod.wakeUp", wakeUp);
    translations.put("authmod.banned", banned);
    translations.put("authmod.wrongPassword", wrongPassword);
    translations.put("authmod.wrongPasswordConfirmation", wrongPasswordConfirmation);
    translations.put("authmod.logged.yes", yes);
    translations.put("authmod.logged.no", no);
    translations.put("authmod.login.usage", loginUsage);
    translations.put("authmod.login.notFound", loginNotFound);
    translations.put("authmod.login.success", loginSuccess);
    translations.put("authmod.logged.usage", loggedUsage);
    translations.put("authmod.login.wrongUUID", loginWrongUuid);
    translations.put("authmod.login.wrongUsername", loginWrongUsername);
    translations.put("authmod.register.exist", registerExist);
    translations.put("authmod.register.usage", registerUsage);
    translations.put("authmod.register.success", registerSuccess);
    translations.put("authmod.changepassword.success", changePasswordSucccess);
    translations.put("authmod.changepassword.samePassword", changePasswordSame);
    translations.put("authmod.changepassword.usage", changePasswordUsage);
    return translations
      .entrySet()
      .stream()
      .filter(e -> !e.getValue().isEmpty())
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
