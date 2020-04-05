package io.chocorean.authmod.command;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.exception.*;
import io.chocorean.authmod.util.text.ServerLanguageMap;

import java.util.HashMap;
import java.util.Map;

public class ExceptionToMessageMapper {

  private final static Map<Class<? extends AuthmodError>, String> messages = new HashMap<>();

  public static void init() {
    messages.put(PlayerAlreadyExistError.class, ServerLanguageMap.getInstance().translateKey("register.exist"));
    messages.put(WrongPasswordConfirmationError.class, ServerLanguageMap.getInstance().translateKey("register.wrongPassword"));
    messages.put(WrongRegisterUsageError.class, ServerLanguageMap.getInstance().translateKey("register.exist"));
    messages.put(WrongPasswordError.class, ServerLanguageMap.getInstance().translateKey("login.wrongPassword"));
    messages.put(BannedPlayerError.class, ServerLanguageMap.getInstance().translateKey("banned"));
    messages.put(WrongLoginUsageError.class, ServerLanguageMap.getInstance().translateKey("login.usage"));
    messages.put(PlayerNotFoundError.class, ServerLanguageMap.getInstance().translateKey("login.notFound"));
    messages.put(AuthmodError.class, ServerLanguageMap.getInstance().translateKey("error"));
  }

  public static String getMessage(Exception e) {
    if(e instanceof AuthmodError) {
      AuthMod.LOGGER.catching(e);
    }
    return messages.getOrDefault(e.getClass(), "Something goes wrong, see the server logs");
  }

}
