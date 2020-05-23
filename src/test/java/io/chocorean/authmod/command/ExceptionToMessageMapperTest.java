package io.chocorean.authmod.command;

import io.chocorean.authmod.core.exception.AuthmodError;
import io.chocorean.authmod.core.exception.WrongLoginUsageError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionToMessageMapperTest {

  @Test
  void testInit(){
    assertDoesNotThrow(ExceptionToMessageMapper::init);
  }

  @Test
  void testGet(){
    ExceptionToMessageMapper.init();
    assertNotNull(ExceptionToMessageMapper.getMessage(new WrongLoginUsageError()));
  }

  @Test
  void testGetDefault() {
    ExceptionToMessageMapper.init();
    assertNotNull(ExceptionToMessageMapper.getMessage(new AuthmodError()));
  }

  @Test
  void testGetDefaultOther() {
    ExceptionToMessageMapper.init();
    assertNotNull(ExceptionToMessageMapper.getMessage(new Exception()));
  }

}
