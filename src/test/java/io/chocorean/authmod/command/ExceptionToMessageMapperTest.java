package io.chocorean.authmod.command;

import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.core.exception.WrongLoginUsageError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionToMessageMapperTest {

  @Test
  public void testInit(){
    ExceptionToMessageMapper.init();
  }

  @Test
  public void testGet(){
    ExceptionToMessageMapper.init();
    ExceptionToMessageMapper.getMessage(new WrongLoginUsageError());
  }

  @Test
  public void testGetDefault() {
    ExceptionToMessageMapper.init();
    assertNotNull(ExceptionToMessageMapper.getMessage(new AuthmodError()));
  }

}
