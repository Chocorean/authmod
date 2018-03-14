package com.chocorean.authmod;

import com.chocorean.authmod.command.LoginCommand;
import com.chocorean.authmod.command.RegisterCommand;
import com.chocorean.authmod.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@Mod(modid = AuthMod.MODID, name = AuthMod.NAME, version = AuthMod.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class AuthMod {
    public static final String MODID = "authmod";
    public static final String NAME = "AuthMod";
    public static final String VERSION = "1.6";
    public static final String COMMON_PROXY = "com.chocorean.authmod.proxy.CommonProxy";
    public static final String CLIENT_PROXY = "com.chocorean.authmod.proxy.ClientProxy";

    @SidedProxy(clientSide = AuthMod.CLIENT_PROXY, serverSide = AuthMod.COMMON_PROXY)
    public static CommonProxy proxy;

    public static final Logger LOGGER = Logger.getLogger(AuthMod.MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File dir = new File("mods/AuthMod");
        dir.mkdir();
        if (!new File("mods/AuthMod/data").exists()) {
            PrintWriter writer;
            try {
                writer = new PrintWriter("mods/AuthMod/data");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {}

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {}

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        LOGGER.info("Registering AuthMod Event Handler");
        MinecraftForge.EVENT_BUS.register(new Handler());
        LOGGER.info("Registering AuthMod Register Command");
        event.registerServerCommand(new RegisterCommand());
        LOGGER.info("Registering AuthMod Login Handler");
        event.registerServerCommand(new LoginCommand());
    }
}
