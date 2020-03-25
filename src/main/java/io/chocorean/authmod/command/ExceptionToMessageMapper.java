package io.chocorean.authmod.command;

import io.chocorean.authmod.config.AuthModConfig;
import io.chocorean.authmod.core.exception.*;

import java.util.HashMap;
import java.util.Map;

public class ExceptionToMessageMapper {

  private final static Map<Class, String> messages = new HashMap<>();

  public static void init() {
    /*
    messages.put(PlayerAlreadyExistError.class, AuthModConfig.i18n.registerExist);
    messages.put(WrongPasswordConfirmationError.class, AuthModConfig.i18n.registerWrongPasswordConfirmation);
    messages.put(InvalidPasswordError.class, AuthModConfig.i18n.registerPasswordTooShort);
    messages.put(WrongRegisterUsageError.class, AuthModConfig.i18n.registerUsage);

    messages.put(WrongUsernameError.class, AuthModConfig.i18n.loginWrongUsername);
    messages.put(WrongPasswordError.class, AuthModConfig.i18n.loginWrongPassword);
    messages.put(BannedPlayerError.class, AuthModConfig.i18n.loginBanned);
    messages.put(PlayerNotFoundError.class, AuthModConfig.i18n.loginUnknown);
    messages.put(InvalidEmailError.class, AuthModConfig.i18n.loginInvalidEmail);
    messages.put(WrongLoginUsageError.class, AuthModConfig.i18n.loginUsage);

    messages.put(AuthmodError.class, AuthModConfig.i18n.error);
    */
  }

  public static String getMessage(AuthmodError e) {
    return messages.getOrDefault(e.getClass(), ""/*AuthModConfig.i18n.error*/);
  }

  public static String getMessage(Exception e) {
    return e.getMessage();
  }
}
