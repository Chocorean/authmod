package io.chocorean.authmod.command;

import io.chocorean.authmod.command.RegisterCommand;
import io.chocorean.authmod.guard.datasource.FileDataSourceStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterCommandTest {

    private RegisterCommand registerCommand;

    @BeforeEach
    void init() {
        this.registerCommand = new RegisterCommand(new FileDataSourceStrategy());
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.registerCommand);
    }

    @Test
    public void testPlayerAlreadyLogged() {
        assertTrue(false);
    }

    @Test
    public void testRegister() {
        assertTrue(false);
    }

    @Test
    public void testRegisterEmailRequired() {
        assertTrue(false);
    }

    @Test
    public void testGetName() {
        assertNotNull(this.registerCommand.getName());
    }

    @Test
    public void testGetUsage() {
        assertNotNull(this.registerCommand.getUsage(null));
    }

    @Test
    public void testGetAliases() {
        assertNotNull(this.registerCommand.getAliases());
    }

    @Test
    public void testCheckPermissions() {
        assertTrue(this.registerCommand.checkPermission(null, null));
    }

    @Test
    public void testGetTabCompletions() {
        assertNotNull(this.registerCommand.getTabCompletions(null, null, null, null));
    }

    @Test
    public void testisUsernameIndex() {
        assertTrue(this.registerCommand.isUsernameIndex(null, 0));
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, this.registerCommand.compareTo(new RegisterCommand(new FileDataSourceStrategy())));
    }

}
