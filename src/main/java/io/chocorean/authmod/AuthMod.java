package io.chocorean.authmod;

import io.chocorean.authmod.authentication.datasource.DatabaseSourceStrategy;
import io.chocorean.authmod.authentication.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.authentication.datasource.IDataSourceStrategy;
import io.chocorean.authmod.command.LoggedCommand;
import io.chocorean.authmod.command.LoginCommand;
import io.chocorean.authmod.command.RegisterCommand;
import io.chocorean.authmod.config.AuthModConfig;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;

@Mod(modid = AuthMod.MODID, name = AuthMod.NAME, version = AuthMod.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class AuthMod {

    static final String MODID = "authmod";
    static final String NAME = "AuthMod";
    public static final String VERSION = "2.5";
    private static final String COMMON_PROXY = "io.chocorean.authmod.proxy.CommonProxy";
    private static final String CLIENT_PROXY = "io.chocorean.authmod.proxy.ClientProxy";
    public static Logger LOGGER = FMLLog.log;
    private static AuthModConfig config;
    private static IDataSourceStrategy strategy;
    @SidedProxy(clientSide = AuthMod.CLIENT_PROXY, serverSide = AuthMod.COMMON_PROXY)
    private static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        AuthMod.LOGGER = event.getModLog();
        config = new AuthModConfig(event.getSuggestedConfigurationFile());
        switch (config.getAuthenticationStrategy().toUpperCase()) {
            case "DATABASE":
                AuthMod.strategy = new DatabaseSourceStrategy(config.getDatabaseConfig());
                LOGGER.info("Now using DatabaseAuthenticationStrategy.");
                break;
            case "FILE":
                AuthMod.strategy = new FileDataSourceStrategy(Paths.get(
                        event.getModConfigurationDirectory().getAbsolutePath(),
                        MODID + "_players.csv").toFile());
                LOGGER.info("Now using FileAuthenticationStrategy.");
                break;
            default:
                AuthMod.strategy = null;
                LOGGER.info("Unknown authentication strategy selected. Nothing will happen.");
        }
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent e) {
        proxy.registerRenderers();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if(strategy != null) {
            if(config.isLoginEnabled()) {
                LOGGER.info("Registering AuthMod Event Handler");
                MinecraftForge.EVENT_BUS.register(new Handler());
                LOGGER.info("Registering AuthMod Login Handler");
                event.registerServerCommand(new LoginCommand(AuthMod.strategy));
                LOGGER.info("Registering AuthMod Logged Handler");
                event.registerServerCommand(new LoggedCommand());
            }
            if(config.isRegisterEnabled()) {
                LOGGER.info("Registering AuthMod Register Handler");
                event.registerServerCommand(new RegisterCommand(AuthMod.strategy));
            }
        }
    }

    public static AuthModConfig getConfig() {
        return AuthMod.config;
    }

    public static IDataSourceStrategy getDataSourceStrategy() {
        return AuthMod.strategy;
    }

}
