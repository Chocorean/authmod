package io.chocorean.authmod.guard.authentication;

import io.chocorean.authmod.guard.IPayload;
import io.chocorean.authmod.guard.validator.EmailValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class LoginPayload implements IPayload {

    @Email
    private String email;

    @Size(min = 5, max = 100)
    @NotNull
    private String password;

    @NotNull
    private String username;

    @Size(min = 32, max = 36)
    private String uuid;

    private Set<ConstraintViolation<IPayload>> errors;


    public LoginPayload() {
        this.errors = new HashSet<>();
    }

    public LoginPayload setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public boolean isValid() {
        return this.isValid(false);
    }

    @Override
    public boolean isValid(boolean emailRequired) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        this.errors = validator.validate(this);
        if(emailRequired && (this.getEmail() == null)) {
            this.errors.add(new EmailValidator());
        }
        return this.errors.isEmpty();
    }

    @Override
    public LoginPayload setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public LoginPayload setEmail(String email) {
        if(email != null)
            email = email.length() < 3 ? null : email;
        this.email = email;
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
