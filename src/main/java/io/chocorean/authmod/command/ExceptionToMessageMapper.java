package io.chocorean.authmod.command;

import io.chocorean.authmod.core.exception.*;
import io.chocorean.authmod.util.text.ServerLanguageMap;

import java.util.HashMap;
import java.util.Map;

public class ExceptionToMessageMapper {

  private final static Map<Class, String> messages = new HashMap<>();

  public static void init() {
    messages.put(PlayerAlreadyExistError.class, ServerLanguageMap.getInstance().translateKey("register.exist"));
    messages.put(WrongPasswordConfirmationError.class, ServerLanguageMap.getInstance().translateKey("register.wrongPassword"));
    messages.put(WrongRegisterUsageError.class, ServerLanguageMap.getInstance().translateKey("register.exist"));
    messages.put(WrongPasswordError.class, ServerLanguageMap.getInstance().translateKey("login.wrongPassword"));
    messages.put(BannedPlayerError.class, ServerLanguageMap.getInstance().translateKey("banned"));
    messages.put(WrongLoginUsageError.class, ServerLanguageMap.getInstance().translateKey("login.usage"));
    messages.put(AuthmodError.class, ServerLanguageMap.getInstance().translateKey("error"));
  }

  public static String getMessage(AuthmodError e) {
    return messages.getOrDefault(e.getClass(), "Oupsss something goes wrong, see the server logs");
  }

  public static String getMessage(Exception e) {
    return e.getMessage();
  }
}
