package io.chocorean.authmod.util.text;

import io.chocorean.authmod.core.i18n.ServerLanguageMap;
import net.minecraft.util.text.StringTextComponent;

public class ServerTranslationTextComponent extends StringTextComponent {

  public ServerTranslationTextComponent(String translationKey, Object... args) {
    super(String.format(ServerLanguageMap.getInstance().getOrDefault(translationKey), args));
  }
}
