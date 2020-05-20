package io.chocorean.authmod.core.validator;

import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.core.exception.WrongPasswordConfirmationError;
import io.chocorean.authmod.core.exception.WrongRegisterUsageError;

public class RegistrationValidator implements ValidatorInterface {

  private final boolean identifierRequired;

  public RegistrationValidator(boolean identifierRequired) {
    this.identifierRequired = identifierRequired;
  }

  @Override
  public boolean validate(PayloadInterface payload) throws AuthmodError {
    int numberOfArgs = this.identifierRequired ? 3 : 2;
    if(payload.getArgs().length != numberOfArgs) {
      throw new WrongRegisterUsageError();
    }
    String password = payload.getArgs()[numberOfArgs - 2];
    String passwordConfirmation = payload.getArgs()[numberOfArgs - 1];
    if(!password.equals(passwordConfirmation)) {
      throw new WrongPasswordConfirmationError();
    }
    return true;
  }

}

