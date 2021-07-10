package io.chocorean.authmod;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Utils {

  public static Path copyFile(Path src) throws IOException {
    String configFile = String.format("%s-server.toml", AuthMod.MODID);
    Path copied = File.createTempFile(Utils.class.getSimpleName(), configFile).toPath();
    Files.copy(src, copied, StandardCopyOption.REPLACE_EXISTING);
    return copied;
  }

  public static void replaceInFile(Path path, String regex, String replacement) throws IOException {
    Charset charset = StandardCharsets.UTF_8;
    String content = new String(Files.readAllBytes(path), charset);
    content = content.replaceAll(regex, replacement);
    Files.write(path, content.getBytes(charset));
  }
}
