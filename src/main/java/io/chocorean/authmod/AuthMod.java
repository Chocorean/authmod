package io.chocorean.authmod;

import io.chocorean.authmod.config.Config;
import io.chocorean.authmod.core.GuardFactory;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.setup.CommandsSetup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ArtifactVersion;

@Mod(AuthMod.MODID)
public class AuthMod {

  public static final String MODID = "authmod";
  public static final String NAME = "AuthMod";
  public static final Logger LOGGER = LogManager.getLogger(NAME);

  public AuthMod() throws Exception {
    ArtifactVersion version = ModLoadingContext.get().getActiveContainer().getModInfo().getVersion();
    LOGGER.info("{} {}", NAME, version);
    LOGGER.info("Register configuration");
    Config.register(ModLoadingContext.get());
    Config.loadConfig();
    MinecraftForge.EVENT_BUS.register(this);
    if (Config.authmodEnabled()) {
      GuardInterface guard = this.createGuard();
      Handler handler = new Handler();
      MinecraftForge.EVENT_BUS.register(handler);
      MinecraftForge.EVENT_BUS.register(new CommandsSetup(handler, guard));
    }
  }

  private GuardInterface createGuard() throws Exception {
    return GuardFactory.createFromConfig(Config.getFactoryConfig());
  }
}
