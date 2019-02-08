package io.chocorean.authmod.guard;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface IPayload {

    IPayload setPassword(String password);
    boolean isValid();
    boolean isValid(boolean emailRequired);
    IPayload setUsername(String username);
    IPayload setUuid(String uuid);
    String getUuid();
    IPayload setEmail(String email);
    String getEmail();
    String getUsername();
    String getPassword();
    Set<ConstraintViolation<IPayload>> getErrors();

}
