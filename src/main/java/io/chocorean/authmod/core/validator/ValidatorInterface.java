package io.chocorean.authmod.core.validator;

import io.chocorean.authmod.core.PayloadInterface;

public interface ValidatorInterface {

  public boolean validate(PayloadInterface payload);

}
