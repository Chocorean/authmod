package io.chocorean.authmod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class AuthModConfig {

  public final DatabaseConfig database;
  private static AuthModConfig config;

  public enum DataSource {FILE, DATABASE}

  public enum Language {en_us, fr_fr}

  public final ForgeConfigSpec.BooleanValue identifierRequired;
  public final ForgeConfigSpec.BooleanValue enableLogin;
  public final ForgeConfigSpec.BooleanValue enableRegister;
  public final ForgeConfigSpec.EnumValue<Language> language;
  public final ForgeConfigSpec.IntValue delay;
  public final ForgeConfigSpec.EnumValue<DataSource> dataSource;

  public AuthModConfig(ForgeConfigSpec.Builder builder) {
    builder.comment("Server configuration settings").push("server");

    this.identifierRequired = builder
      .comment("Identifier must be provided for registration and authentication")
      .define("identifierRequired", false);

    this.enableLogin = builder
      .comment("Enable or disable the /login command. If disabled, the server will be opened to everyone).")
      .define("enableLogin", false);

    this.enableRegister = builder
      .comment("Enable or disable the /register command.")
      .define("enableRegister", false);

    this.delay = builder
      .comment("delay in seconds a player can authenticate before being automatically kicked from the server.")
      .defineInRange("delay", 640, 1, 1024);

    this.language = builder
      .comment("lang file to use")
      .defineEnum("dataSource", Language.en_us);

    this.dataSource = builder.comment("The way you want to store player's data, choose between 'database' or 'file'. If the strategy is unknown, the server will be open for everyone.")
      .defineEnum("dataSource", DataSource.FILE);
    builder.pop();

    this.database = new DatabaseConfig(builder);
  }

  public static void register(final ModLoadingContext context) {
    final Pair<AuthModConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(AuthModConfig::new);
    context.registerConfig(ModConfig.Type.SERVER, specPair.getRight());
    config = specPair.getLeft();
  }

  public static AuthModConfig get() {
    return config;
  }

}
