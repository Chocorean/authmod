package io.chocorean.authmod.command;

import io.chocorean.authmod.command.LoginCommand;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.guard.datasource.FileDataSourceStrategy;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoginCommandTest {

    private LoginCommand loginCommand;

    @BeforeEach
    void init() {
        this.loginCommand = new LoginCommand(new FileDataSourceStrategy());
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.loginCommand);
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
    public void testLoginEmailRequired() {
        assertTrue(false);
    }

    @Test
    public void testGetName() {
        assertNotNull(this.loginCommand.getName());
    }

    @Test
    public void testGetUsage() {
        assertNotNull(this.loginCommand.getUsage(null));
    }

    @Test
    public void testGetAliases() {
        assertNotNull(this.loginCommand.getAliases());
    }

    @Test
    public void testCheckPermissions() {
        assertTrue(this.loginCommand.checkPermission(null, null));
    }

    @Test
    public void testGetTabCompletions() {
        assertNotNull(this.loginCommand.getTabCompletions(null, null, null, null));
    }

    @Test
    public void testisUsernameIndex() {
        assertTrue(this.loginCommand.isUsernameIndex(null, 0));
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, this.loginCommand.compareTo(new LoginCommand(new FileDataSourceStrategy())));
    }

}
