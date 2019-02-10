package io.chocorean.authmod.command;

import com.mojang.authlib.GameProfile;

import io.chocorean.authmod.PlayerFactory;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.exception.AuthmodException;
import io.chocorean.authmod.guard.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.guard.datasource.IDataSourceStrategy;
import io.chocorean.authmod.guard.registration.Registrator;
import io.chocorean.authmod.model.IPlayer;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterCommandTest {
    private RegisterCommand registerCommand;
    private Handler handler;
    private IDataSourceStrategy dataSourceStrategy;
    private File dataFile;
    private IPlayer player;
    private EntityPlayerMP sender;
    private Registrator registrator;

    @BeforeEach
    void init() {
        this.dataFile = Paths.get(
            System.getProperty("java.io.tmpdir"),
            "authmod.csv"
        ).toFile();
        if (dataFile.exists())
        dataFile.delete();
        this.handler = new Handler();
        this.player = PlayerFactory.create();
        this.sender = mock(EntityPlayerMP.class);

        when(this.sender.getGameProfile()).thenReturn(
            new GameProfile(
                UUID.fromString(player.getUuid()),
                player.getUsername()
            )
        );
        when(this.sender.getDisplayNameString()).thenReturn(
            player.getUsername()
        );
        NetHandlerPlayServer conn = mock(NetHandlerPlayServer.class);
        ((EntityPlayerMP) sender).connection = conn;
        this.dataSourceStrategy = new FileDataSourceStrategy(this.dataFile);
        this.registrator = new Registrator(this.dataSourceStrategy);
        this.registerCommand = new RegisterCommand(
            this.handler,
            this.dataSourceStrategy
        );
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.registerCommand);
    }

    @Test
    public void testExecuteWrongNumberOfArgs() {
        this.registerCommand.execute(
            null,
            sender,
            new String[] {
                "test",
                "test2",
                player.getEmail(),
                player.getPassword()
            }
        );
        assertNull(
            this.dataSourceStrategy.find(null, this.player.getUsername())
        );
        assertFalse(this.handler.isLogged(this.sender));
    }

    @Test
    public void testExecute() {
        this.registrator = new Registrator(this.dataSourceStrategy);
        this.registerCommand.execute(
            null,
            sender,
            new String[] { player.getPassword(), player.getPassword() }
        );
        assertNotNull(
            this.dataSourceStrategy.find(null, this.player.getUsername())
        );
        assertTrue(this.handler.isLogged(this.sender));
    }

    @Test
    public void testExecuteWithEmailRequired() {
        this.registerCommand = new RegisterCommand(
            this.handler,
            this.dataSourceStrategy,
            true
        );
        this.registrator = new Registrator(this.dataSourceStrategy);
        this.registerCommand.execute(
            null,
            sender,
            new String[] {
                player.getEmail(),
                player.getPassword(),
                player.getPassword()
            }
        );
        assertNotNull(
            this.dataSourceStrategy.find(this.player.getEmail(), null)
        );
        assertTrue(this.handler.isLogged(this.sender));
    }

    @Test
    public void testExecuteWithEmailRequiredIncorrect() {
        this.registerCommand = new RegisterCommand(
            this.handler,
            this.dataSourceStrategy,
            true
        );
        this.registrator = new Registrator(this.dataSourceStrategy);
        this.registerCommand.execute(
            null,
            sender,
            new String[] { player.getPassword(), player.getPassword() }
        );
        assertNull(this.dataSourceStrategy.find(this.player.getEmail(), null));
        assertFalse(this.handler.isLogged(this.sender));
    }

    @Test
    public void testExecuteEmailRequiredIncorrect() {
        assertFalse(this.handler.isLogged(this.sender));
    }

    @Test
    public void testExecuteAlreadyLogged() throws AuthmodException {
        handler.authorizePlayer(sender);
        assertTrue(this.handler.isLogged(this.sender));
        this.registerCommand.execute(
            null,
            sender,
            new String[] { player.getPassword(), player.getPassword() }
        );
        assertTrue(this.handler.isLogged(this.sender));
    }

    @Test
    public void testExecutePlayerAlreadyExists() throws AuthmodException {
        this.registrator.register(
            io.chocorean.authmod.guard.PlayerFactory.createRegistrationFactoryFromPlayer(
                player
            )
        );
        this.registerCommand.execute(
            null,
            sender,
            new String[] { player.getPassword(), player.getPassword() }
        );
        assertFalse(this.handler.isLogged(this.sender));
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
        assertNotNull(
            this.registerCommand.getTabCompletions(null, null, null, null)
        );
    }

    @Test
    public void testisUsernameIndex() {
        assertTrue(this.registerCommand.isUsernameIndex(null, 0));
    }

    @Test
    public void testCompareTo() {
        assertEquals(
            0,
            this.registerCommand.compareTo(
                new RegisterCommand(null, new FileDataSourceStrategy())
            )
        );
    }

}

