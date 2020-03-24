package io.chocorean.authmod.core.validator;

import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.core.exception.AuthmodError;

public interface ValidatorInterface {

  boolean validate(PayloadInterface payload) throws AuthmodError;

}
