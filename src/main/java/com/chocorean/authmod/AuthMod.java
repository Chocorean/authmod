package com.chocorean.authmod;

import com.chocorean.authmod.authentification.DatabaseAuthenticationStrategy;
import com.chocorean.authmod.authentification.IAuthenticationStrategy;
import com.chocorean.authmod.commands.LoginCommand;
import com.chocorean.authmod.events.Handler;
import com.chocorean.authmod.events.PlayerDescriptor;
import com.chocorean.authmod.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.HashMap;

@Mod(modid = AuthMod.MODID, name = AuthMod.NAME, version = AuthMod.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class AuthMod {

    public static final String MODID = "authmod";
    public static final String NAME = "AuthMod";
    public static final String VERSION = "1.6";
    public static final String COMMON_PROXY = "com.chocorean.authmod.proxy.CommonProxy";
    public static final String CLIENT_PROXY = "com.chocorean.authmod.proxy.ClientProxy";
    public static final org.apache.logging.log4j.Logger LOGGER = FMLLog.getLogger();
    public static final HashMap<EntityPlayer, PlayerDescriptor> descriptors = new HashMap<>();
    @SidedProxy(clientSide = AuthMod.CLIENT_PROXY, serverSide = AuthMod.COMMON_PROXY)
    private static CommonProxy proxy;

    /*
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) { }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) { }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) { }
    */

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

    public static IAuthenticationStrategy getAuthenticationStrategy() {
        return new DatabaseAuthenticationStrategy();

    }
}
