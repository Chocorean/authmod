package io.chocorean.authmod.config;

import static io.chocorean.authmod.core.datasource.DataSourceStrategyInterface.Strategy.FILE;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.FactoryConfig;
import io.chocorean.authmod.core.datasource.DataSourceStrategyInterface;
import io.chocorean.authmod.core.datasource.DatabaseStrategy;
import io.chocorean.authmod.core.i18n.ServerLanguageMap;
import java.nio.file.Path;
import java.util.*;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

  private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
  private static final ForgeConfigSpec SPEC;
  public static final DatabaseConfig database;
  public static final I18nConfig i18n;
  public static final ForgeConfigSpec.BooleanValue enableLogin;
  public static final ForgeConfigSpec.BooleanValue enableRegister;
  public static final ForgeConfigSpec.BooleanValue enableChangePassword;
  public static final ForgeConfigSpec.EnumValue<ServerLanguageMap.Language> language;
  public static final ForgeConfigSpec.IntValue delay;
  public static final ForgeConfigSpec.EnumValue<DataSourceStrategyInterface.Strategy> dataSource;
  public static final ForgeConfigSpec.ConfigValue<List<? extends String>> commandWhitelist;

  private Config(){}

  static {
    BUILDER.comment("Server configuration settings").push("server");
    enableLogin =
      BUILDER
        .comment("Enable or disable the /login command. If disabled, the server will be opened to everyone).")
        .define("enableLogin", false);

    enableRegister = BUILDER.comment("Enable or disable the /register command.").define("enableRegister", false);

    enableChangePassword =
      BUILDER.comment("Enable or disable the /changepassword command.").define("enableChangePassword", false);

    delay =
      BUILDER
        .comment("delay in seconds a player can authenticate before being automatically kicked from the server.")
        .defineInRange("delay", 60, 1, 1024);

    language = BUILDER.comment("lang file to use").defineEnum("language", ServerLanguageMap.Language.EN_US);

    dataSource =
      BUILDER
        .comment(
          "The way you want to store player's data, choose between 'database' or 'file'. If the strategy is unknown, the server will be open for everyone."
        )
        .defineEnum("dataSource", FILE);

    String[] whitelist = { "register", "login", "logged", "help" };
    commandWhitelist =
      BUILDER
        .comment("Whitelisted commands (can be used without being logged)")
        .defineList("whitelist", Arrays.asList(whitelist), x -> true);
    BUILDER.pop();

    database = new DatabaseConfig(BUILDER);
    i18n = new I18nConfig(BUILDER);
    SPEC = BUILDER.build();
  }

  public static boolean authmodEnabled() {
    return enableLogin.get() || enableRegister.get();
  }

  public static void register(final ModLoadingContext context) {
    context.registerConfig(ModConfig.Type.SERVER, SPEC);
  }

  @SubscribeEvent
  public static void onLoad(final ModConfig.Loading configEvent) {
    setup();
  }

  @SubscribeEvent
  public static void onReload(final ModConfig.Reloading configEvent) {
    setup();
  }

  private static void setup() {
    ServerLanguageMap.loadTranslations(language.get().name());
    ServerLanguageMap.replaceTranslations(i18n.getTranslations());
  }

  public static void loadConfig() {
    loadConfig(getConfigurationFile());
  }

  public static void loadConfig(Path path) {
    if (path != null) {
      try {
        final CommentedFileConfig file = CommentedFileConfig
          .builder(path.toFile())
          .sync()
          .autosave()
          .writingMode(WritingMode.REPLACE)
          .build();
        file.load();
        SPEC.setConfig(file);
      } catch (Exception e) {
        AuthMod.LOGGER.catching(e);
      }
    }
  }

  public static Path getConfigurationFile() {
    return FMLPaths.CONFIGDIR.get().resolve("../world/serverconfig/authmod-server.toml").normalize();
  }

  public static FactoryConfig getFactoryConfig() {
    Map<DatabaseStrategy.Column, String> columns = new EnumMap<>(DatabaseStrategy.Column.class);
    for (DatabaseStrategy.Column c : DatabaseStrategy.Column.values()) {
      columns.put(c, Config.database.columns.get(c).get());
    }
    return new FactoryConfig()
      .setConfigDirectory(getConfigurationFile().resolve("..").normalize())
      .setStrategy(Config.dataSource.get())
      .setDialect(database.dialect.get())
      .setDatabase(database.database.get())
      .setTable(database.table.get())
      .setHost(database.host.get())
      .setPort(database.port.get())
      .setUser(database.user.get())
      .setDriver(database.driver.get())
      .setPassword(database.password.get())
      .setColumns(columns);
  }
}
