package io.chocorean.authmod.guard;

import io.chocorean.authmod.exception.InvalidEmailException;

import javax.validation.ConstraintViolation;

public class MappingConstraintViolationsToExceptions {

    public static void throwException(ConstraintViolation<IPayload> c) throws InvalidEmailException {
        if(getPropery(c).equals("email") && getAnnotationType(c).equals(javax.validation.constraints.Email.class)) {
            throw new InvalidEmailException("test");
        }
    }

    private static Class getAnnotationType(ConstraintViolation<IPayload> c) {
        return c.getConstraintDescriptor().getAnnotation().annotationType();
    }

    private static String getPropery(ConstraintViolation<IPayload> c) {
        return c.getPropertyPath().toString();
    }
}
