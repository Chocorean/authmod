package io.chocorean.authmod.setup;

import io.chocorean.authmod.AuthMod;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ArtifactVersion;

public class UpdateSetup {

  private static final String VERSION_URL = "https://raw.githubusercontent.com/Chocorean/authmod/master/build.gradle";
  private static final Pattern PATTERN_VERSION = Pattern.compile("\\s*version\\s*=\\s*'(.+)'");
  public static final Logger LOGGER = AuthMod.LOGGER;

  public static void checkForUpdates(ArtifactVersion current) {
    try (BufferedInputStream in = new BufferedInputStream(new URL(VERSION_URL).openStream())) {
      byte[] dataBuffer = new byte[1024];
      StringBuilder sb = new StringBuilder();
      while ((in.read(dataBuffer, 0, 1024)) != -1) {
        sb.append(new String(dataBuffer));
      }
      Matcher matcher = PATTERN_VERSION.matcher(sb.toString());
      if (matcher.find()) {
        String version = matcher.group(1);
        if (!version.equals(current.toString())) {
          LOGGER.warn("An update is available! '{}' -> '{}'", current, version);
        }
      }
    } catch (IOException e) {
      LOGGER.warn("Cannot check new releases", e);
    }
  }
}
