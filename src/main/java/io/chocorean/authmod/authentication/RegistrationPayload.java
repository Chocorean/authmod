package io.chocorean.authmod.authentication;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.AssertTrue;
import java.util.Set;

public class RegistrationPayload implements IPayload {
    private LoginPayload payload;
    private String passwordConfirmation;

    public RegistrationPayload() {
        this.payload = new LoginPayload();
    }

    @Override
    public RegistrationPayload setPassword(String password) {
        this.payload.setPassword(password);
        return this;
    }

    @Override
    public boolean isValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<LoginPayload>> violationsLogin = validator.validate(this.payload);
        Set<ConstraintViolation<RegistrationPayload>> violationsRegistration = validator.validate(this);
        return violationsLogin.isEmpty() && violationsRegistration.isEmpty();
    }

    @Override
    public RegistrationPayload setUsername(String username) {
        this.payload.setUsername(username);
        return this;
    }

    @Override
    public IPayload setUuid(String uuid) {
        this.payload.setUuid(uuid);
        return this;
    }

    @Override
    public String getUuid() {
        return this.payload.getUuid();
    }

    @Override
    public RegistrationPayload setEmail(String email) {
        this.payload.setEmail(email);
        return this;
    }

    @AssertTrue
    private boolean isPasswordConfirmationMatchs() {
        return this.passwordConfirmation.equals(this.payload.getPassword());
    }

    @Override
    public String getEmail() {
        return this.payload.getEmail();
    }

    @Override
    public String getUsername() {
        return this.payload.getUsername();
    }

    @Override
    public String getPassword() {
        return this.payload.getPassword();
    }

    public String getPasswordConfirmation() {
        return this.passwordConfirmation;
    }

    public RegistrationPayload setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
        return this;
    }
}
