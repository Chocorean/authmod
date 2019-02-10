package io.chocorean.authmod.guard;

import io.chocorean.authmod.exception.InvalidEmailException;
import io.chocorean.authmod.guard.payload.IPayload;
import javax.validation.ConstraintViolation;

public class MappingConstraintViolationsToExceptions {

  private MappingConstraintViolationsToExceptions() {}

  public static void throwException(ConstraintViolation<IPayload> c) throws InvalidEmailException {
    if (getProperty(c).equals("email")
        && getAnnotationType(c).equals(javax.validation.constraints.Email.class)) {
      throw new InvalidEmailException("test");
    }
  }

  private static Class getAnnotationType(ConstraintViolation<IPayload> c) {
    return c.getConstraintDescriptor().getAnnotation().annotationType();
  }

  private static String getProperty(ConstraintViolation<IPayload> c) {
    return c.getPropertyPath().toString();
  }
}
