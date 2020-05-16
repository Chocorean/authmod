package io.chocorean.authmod.core.validator;

import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.core.exception.WrongLoginUsageError;

public class LoginValidator implements ValidatorInterface {

  private final boolean indentifierRequired;

  public LoginValidator(boolean identifierRequired) {
    this.indentifierRequired = identifierRequired;
  }

  @Override
  public boolean validate(PayloadInterface payload) throws AuthmodError {
    int numberOfArgs = this.indentifierRequired ? 2 : 1;
    if(payload.getArgs().length != numberOfArgs) {
      throw new WrongLoginUsageError();
    }
    return true;
  }

}

