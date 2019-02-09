package io.chocorean.authmod.guard.payload;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface IPayload {

    IPayload setPassword(String password);
    boolean isValid();
    IPayload setUsername(String username);
    IPayload setUuid(String uuid);
    String getUuid();
    IPayload setEmail(String email);
    boolean isEmailRequired();
    IPayload setEmailRequired(boolean emailRequired);
    String getEmail();
    String getUsername();
    String getPassword();
    Set<ConstraintViolation<IPayload>> getErrors();

}
