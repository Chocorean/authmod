package io.chocorean.authmod.util.text;

import io.chocorean.authmod.core.i18n.ServerLanguageMap;
import net.minecraft.util.text.TextComponentString;

public class ServerTranslationTextComponent extends TextComponentString {

  public ServerTranslationTextComponent(String translationKey, Object... args) {
    super(String.format(ServerLanguageMap.getInstance().getOrDefault(translationKey), args));
  }
}
