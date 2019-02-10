package io.chocorean.authmod.guard.payload;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginPayload implements IPayload {
    @Email
    private String email;

    @NotNull
    @Size(min = 5, max = 100)
    private String password;

    @NotNull
    private String username;

    @Size(min = 32, max = 36)
    private String uuid;

    private Set<ConstraintViolation<IPayload>> errors;

    private boolean emailRequired;

    public LoginPayload() {
        this(false);
    }

    public LoginPayload(boolean emailRequired) {
        this.errors = new HashSet<>();
    }

    public LoginPayload setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public boolean isValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        this.errors = validator.validate(this);
        return this.errors.isEmpty();
    }

    @Override
    public LoginPayload setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public LoginPayload setEmail(String email) {
        if (email != null)
        email = email.length() < 3 ? null : email;
        this.email = email;
        return this;
    }

    @Override
    public boolean isEmailRequired() {
        return this.emailRequired;
    }

    @Override
    public IPayload setEmailRequired(boolean emailRequired) {
        this.emailRequired = emailRequired;
        return this;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Set<ConstraintViolation<IPayload>> getErrors() {
        return this.errors;
    }

    @AssertTrue
    private boolean isEmailDefined() {
        return !this.emailRequired || this.getEmail() != null;
    }

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public LoginPayload setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

}

