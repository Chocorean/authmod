package io.chocorean.authmod.command;

import io.chocorean.authmod.command.LoggedCommand;
import io.chocorean.authmod.guard.datasource.FileDataSourceStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoggedCommandTest {

    private LoggedCommand loggedCommand;

    @BeforeEach
    void init() {
        this.loggedCommand = new LoggedCommand();
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.loggedCommand);
    }

    @Test
    public void testPlayerAlreadyLogged() {
        assertTrue(false);
    }

    @Test
    public void testPlayerNotLogged() {
        assertTrue(false);
    }

    @Test
    public void testGetName() {
        assertNotNull(this.loggedCommand.getName());
    }

    @Test
    public void testGetUsage() {
        assertNotNull(this.loggedCommand.getUsage(null));
    }

    @Test
    public void testGetAliases() {
        assertNotNull(this.loggedCommand.getAliases());
    }

    @Test
    public void testCheckPermissions() {
        assertTrue(this.loggedCommand.checkPermission(null, null));
    }

    @Test
    public void testGetTabCompletions() {
        assertNotNull(this.loggedCommand.getTabCompletions(null, null, null, null));
    }

    @Test
    public void testisUsernameIndex() {
        assertTrue(this.loggedCommand.isUsernameIndex(null, 0));
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, this.loggedCommand.compareTo(new LoggedCommand()));
    }

}
