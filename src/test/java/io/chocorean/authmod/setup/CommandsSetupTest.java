package io.chocorean.authmod.setup;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import io.chocorean.authmod.event.Handler;
import net.minecraftforge.event.RegisterCommandsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandsSetupTest {

  private CommandsSetup setup;

  @BeforeEach
  void init() {
    this.setup = new CommandsSetup(new Handler(), null);
  }

  @Test
  public void testConstructor() {
    assertNotNull(this.setup);
  }

  @Test
  public void testOnRegisterCommands() throws Exception {
    RegisterCommandsEvent event = mock(RegisterCommandsEvent.class);
    this.setup.onCommandsRegister(event);
  }
}
