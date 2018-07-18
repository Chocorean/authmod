package io.chocorean.authmod;

import io.chocorean.authmod.authentication.DatabaseAuthenticationStrategy;
import io.chocorean.authmod.authentication.FileAuthenticationStrategy;
import io.chocorean.authmod.authentication.IAuthenticationStrategy;
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

import java.nio.file.Paths;

@Mod(modid = AuthMod.MODID, name = AuthMod.NAME, version = AuthMod.VERSION, serverSideOnly = false, acceptableRemoteVersions = "*")
public class AuthMod {

    static final String MODID = "authmod";
    static final String NAME = "AuthMod";
    static final String VERSION = "1.6";
    private static final String COMMON_PROXY = "io.chocorean.authmod.proxy.CommonProxy";
    private static final String CLIENT_PROXY = "io.chocorean.authmod.proxy.ClientProxy";
    private static final org.apache.logging.log4j.Logger LOGGER = FMLLog.getLogger();
    private static AuthModConfig config;
    private static IAuthenticationStrategy strategy;
    @SidedProxy(clientSide = AuthMod.CLIENT_PROXY, serverSide = AuthMod.COMMON_PROXY)
    private static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new AuthModConfig(event.getSuggestedConfigurationFile());
        switch (config.getAuthenticationStrategy().toUpperCase()) {
            case "DATABASE":
                strategy = new DatabaseAuthenticationStrategy();
                LOGGER.info("Use DatabaseAuthenticationStrategy");
                break;
            case "FILE":
                strategy = new FileAuthenticationStrategy(Paths.get(
                        event.getModConfigurationDirectory().getAbsolutePath(),
                        MODID + "_players.csv").toFile());
                LOGGER.info("Use FileAuthenticationStrategy");
                break;
            default:
                strategy = null;
                LOGGER.info("No Authentication strategy selected");
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

    public static IAuthenticationStrategy getAuthenticationStrategy() {
        return AuthMod.strategy;
    }

}
