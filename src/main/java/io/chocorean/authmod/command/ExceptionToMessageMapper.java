package io.chocorean.authmod.command;

import io.chocorean.authmod.core.exception.*;
import io.chocorean.authmod.util.text.ServerLanguageMap;

import java.util.HashMap;
import java.util.Map;

public class ExceptionToMessageMapper {

  private ExceptionToMessageMapper() {}

  public static void init() {
    messages.put(WrongPasswordError.class, ServerLanguageMap.getInstance().translateKey("wrongPassword"));
    messages.put(WrongPasswordConfirmationError.class, ServerLanguageMap.getInstance().translateKey("wrongPasswordConfirmation"));
    messages.put(BannedPlayerError.class, ServerLanguageMap.getInstance().translateKey("banned"));
    messages.put(WrongLoginUsageError.class, ServerLanguageMap.getInstance().translateKey("login.usage"));
    messages.put(PlayerNotFoundError.class, ServerLanguageMap.getInstance().translateKey("login.notFound"));
    messages.put(PlayerAlreadyExistError.class, ServerLanguageMap.getInstance().translateKey("register.exist"));
    messages.put(WrongRegisterUsageError.class, ServerLanguageMap.getInstance().translateKey("register.usage"));
    messages.put(SamePasswordError.class, ServerLanguageMap.getInstance().translateKey("changepassword.samePassword"));
    messages.put(AuthmodError.class, ServerLanguageMap.getInstance().translateKey("error"));
  }

  public static String getMessage(Exception e) {
    return messages.getOrDefault(e.getClass(), "Something goes wrong, see the server logs");
  }

  private final static Map<Class<? extends AuthmodError>, String> messages = new HashMap<>();

}
