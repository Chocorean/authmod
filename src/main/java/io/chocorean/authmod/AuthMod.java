package io.chocorean.authmod;

import io.chocorean.authmod.authentification.DatabaseAuthenticationStrategy;
import io.chocorean.authmod.authentification.FileAuthenticationStrategy;
import io.chocorean.authmod.authentification.IAuthenticationStrategy;
import io.chocorean.authmod.authentification.PublicServerAuthenticationStrategy;
import io.chocorean.authmod.command.LoginCommand;
import io.chocorean.authmod.config.AuthModConfig;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.event.PlayerDescriptor;
import io.chocorean.authmod.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.HashMap;
import java.util.Map;

@Mod(modid = AuthMod.MODID, name = AuthMod.NAME, version = AuthMod.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class AuthMod {

    static final String MODID = "authmod";
    static final String NAME = "AuthMod";
    static final String VERSION = "1.6";
    private static final String COMMON_PROXY = "io.chocorean.authmod.proxy.CommonProxy";
    private static final String CLIENT_PROXY = "io.chocorean.authmod.proxy.ClientProxy";
    private static final org.apache.logging.log4j.Logger LOGGER = FMLLog.getLogger();
    public static final Map<EntityPlayer, PlayerDescriptor> descriptors = new HashMap<>();
    public static AuthModConfig config;
    public static IAuthenticationStrategy strategy;
    @SidedProxy(clientSide = AuthMod.CLIENT_PROXY, serverSide = AuthMod.COMMON_PROXY)
    private static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new AuthModConfig(event.getSuggestedConfigurationFile());
        config.load();
        switch (config.getAuthenticationStrategy().toUpperCase()) {
            case "DATABASE":
                strategy = new DatabaseAuthenticationStrategy();
                LOGGER.info("Use DatabaseAuthenticationStrategy");
                break;
            case "FILE":
                strategy = new FileAuthenticationStrategy();
                LOGGER.info("Use FileAuthenticationStrategy");
            default:
                strategy = new PublicServerAuthenticationStrategy();
                LOGGER.info("Use PublicServerAuthenticationStrategy");
        }
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent e) {
        proxy.registerRenderers();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        LOGGER.info("Registering AuthMod Event Handler");
        MinecraftForge.EVENT_BUS.register(new Handler());
        LOGGER.info("Registering AuthMod Login Handler");
        event.registerServerCommand(new LoginCommand());
    }

}
