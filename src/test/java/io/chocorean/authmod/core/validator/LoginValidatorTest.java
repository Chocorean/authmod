package io.chocorean.authmod.core.validator;

import io.chocorean.authmod.core.Payload;
import io.chocorean.authmod.core.Player;
import io.chocorean.authmod.core.PlayerInterface;
import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.core.exception.WrongLoginUsageError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginValidatorTest {

  private ValidatorInterface validator;
  private PlayerInterface player;

  @BeforeEach
  void init() {
    this.validator = new LoginValidator(false);
    this.player = new Player(null, null);
  }

  @Test
  void testValidate() throws AuthmodError {
    assertTrue(this.validator.validate(new Payload(this.player, new String[]{"Perrier"})));
  }

  @Test
  void testValidateWrongNumberOfArgs() {
    assertThrows(WrongLoginUsageError.class, () -> this.validator.validate(new Payload(this.player, new String[]{})));
  }

}
