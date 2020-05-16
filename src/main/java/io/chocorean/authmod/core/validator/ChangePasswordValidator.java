package io.chocorean.authmod.core.validator;

import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.core.exception.SamePasswordError;
import io.chocorean.authmod.core.exception.WrongPasswordConfirmationError;

public class ChangePasswordValidator implements ValidatorInterface {

  @Override
  public boolean validate(PayloadInterface payload) throws AuthmodError {
    if(payload.getArgs().length != 3)
      return false;

    if(!payload.getArgs()[1].contentEquals(payload.getArgs()[2]))
      throw new WrongPasswordConfirmationError();

    if(payload.getArgs()[0].contentEquals(payload.getArgs()[1]))
      throw new SamePasswordError();
    return true;
  }

}

