package io.chocorean.authmod;

import io.chocorean.authmod.config.Config;
import io.chocorean.authmod.core.GuardFactory;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.setup.CommandsSetup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = AuthMod.MODID, name = AuthMod.NAME, version = AuthMod.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class AuthMod {

  public static final String MODID = "authmod";
  static final String NAME = "AuthMod";
  static final String VERSION = "1.0.0";
  public static final Logger LOGGER = LogManager.getLogger(NAME);

  public AuthMod() {
    LOGGER.info("{} {}", NAME, VERSION);
    LOGGER.info("Register configuration");
  }

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    Config.load();
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Mod.EventHandler
  public void onCommandsRegister(FMLServerStartingEvent event) throws Exception {
    if (Config.authmodEnabled()) {
      GuardInterface guard = this.createGuard();
      Handler handler = new Handler();
      MinecraftForge.EVENT_BUS.register(handler);
      new CommandsSetup(handler, guard).registerCommands(event);
    }
  }

  private GuardInterface createGuard() throws Exception {
    return GuardFactory.createFromConfig(Config.getFactoryConfig());
  }
}
