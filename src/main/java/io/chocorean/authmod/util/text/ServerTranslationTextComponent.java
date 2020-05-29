package io.chocorean.authmod.util.text;

import net.minecraft.util.text.TranslationTextComponent;

public class ServerTranslationTextComponent extends TranslationTextComponent {

  public ServerTranslationTextComponent(String translationKey, Object... args) {
    super(ServerLanguageMap.getInstance().translateKey(translationKey), args);
  }

}
