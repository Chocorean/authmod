package com.chocorean.authmod;

import com.chocorean.authmod.command.LoginCommand;
import com.chocorean.authmod.command.RegisterCommand;
import com.chocorean.authmod.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Mod(modid = AuthMod.MODID, version = AuthMod.VERSION, serverSideOnly = true)
public class AuthMod {
    public static final String MODID = "authmod";
    public static final String VERSION = "1.0";
    public static final String COMMON_PROXY = "com.chocorean.authmod.proxy.CommonProxy";
    public static final String CLIENT_PROXY = "com.chocorean.authmod.proxy.ClientProxy";

    @SidedProxy(clientSide = AuthMod.CLIENT_PROXY, serverSide = AuthMod.COMMON_PROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preload(FMLPreInitializationEvent event) {
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
    public void serverStarting(FMLServerStartingEvent event) {
        MinecraftForge.EVENT_BUS.register(new Handler());
        event.registerServerCommand(new RegisterCommand());
        event.registerServerCommand(new LoginCommand());
    }
}
