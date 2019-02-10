package io.chocorean.authmod.guard.payload;

import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

public class RegistrationPayload implements IPayload {
  @NotNull private final LoginPayload payload;

  @NotNull private String passwordConfirmation;
  private Set<ConstraintViolation<IPayload>> errors;

  public RegistrationPayload() {
    this.payload = new LoginPayload();
    this.errors = new HashSet<>();
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
    this.errors = validator.validate(this.payload);
    this.errors.addAll(validator.validate(this));
    return this.errors.isEmpty();
  }

  @Override
  public RegistrationPayload setUsername(String username) {
    this.payload.setUsername(username);
    return this;
  }

  @Override
  public RegistrationPayload setUuid(String uuid) {
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

  @Override
  public boolean isEmailRequired() {
    return this.payload.isEmailRequired();
  }

  @Override
  public RegistrationPayload setEmailRequired(boolean emailRequired) {
    this.payload.setEmailRequired(emailRequired);
    return this;
  }

  @AssertTrue
  private boolean isEmailValid() {
    return !this.isEmailRequired() || this.payload.getEmail() != null;
  }

  @AssertTrue
  private boolean isEmailDefined() {
    return !this.isEmailRequired() || this.payload.getEmail() != null;
  }

  @AssertTrue
  private boolean isPasswordConfirmationMatches() {
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

  @Override
  public Set<ConstraintViolation<IPayload>> getErrors() {
    return this.errors;
  }

  public RegistrationPayload setPasswordConfirmation(String passwordConfirmation) {
    this.passwordConfirmation = passwordConfirmation;
    return this;
  }
}
