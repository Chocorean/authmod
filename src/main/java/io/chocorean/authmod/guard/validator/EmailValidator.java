package io.chocorean.authmod.guard.validator;

import io.chocorean.authmod.guard.IPayload;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;

public class EmailValidator implements ConstraintViolation<IPayload> {

    @Override
    public String getMessage() { return null; }

    @Override
    public String getMessageTemplate() { return null; }

    @Override
    public IPayload getRootBean() { return null; }

    @Override
    public Class<IPayload> getRootBeanClass() { return null; }

    @Override
    public Object getLeafBean() { return null; }

    @Override
    public Object[] getExecutableParameters() { return new Object[0]; }

    @Override
    public Object getExecutableReturnValue() { return null; }

    @Override
    public Path getPropertyPath() { return null; }

    @Override
    public Object getInvalidValue() { return null; }

    @Override
    public ConstraintDescriptor<?> getConstraintDescriptor() { return null; }

    @Override
    public <U> U unwrap(Class<U> type) { return null; }
}


