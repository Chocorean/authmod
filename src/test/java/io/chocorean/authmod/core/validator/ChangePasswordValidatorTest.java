package io.chocorean.authmod.core.validator;

import io.chocorean.authmod.core.Payload;
import io.chocorean.authmod.core.Player;
import io.chocorean.authmod.core.PlayerInterface;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.core.exception.SamePasswordError;
import io.chocorean.authmod.core.exception.WrongPasswordConfirmationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChangePasswordValidatorTest {

  private ChangePasswordValidator validator;
  private PlayerInterface player;

  @BeforeEach
  void init() {
    this.validator = new ChangePasswordValidator();
    this.player = new Player(null, null);
  }

  @Test
  public void testValidate() throws AuthmodError {
    assertTrue(this.validator.validate(new Payload(this.player, new String[]{"Expelliarmus", "Obliviate" ,"Obliviate"})));
  }

  @Test
  public void testValidateWrongNumberOfArgs() throws AuthmodError {
    assertFalse(this.validator.validate(new Payload(this.player, new String[]{})));
  }

  @Test
  public void testValidateDifferentPasswords() throws AuthmodError {
    assertThrows(
      WrongPasswordConfirmationError.class,
      () -> this.validator.validate(new Payload(this.player, new String[]{"Expelliarmus", "Lumos" ,"Lumox"})));
  }

  @Test
  public void testValidateSamePasswords() throws AuthmodError {
    assertThrows(
      SamePasswordError.class,
      () -> this.validator.validate(new Payload(this.player, new String[]{"Avada Kedavra", "Avada Kedavra" ,"Avada Kedavra"})));
  }

}
