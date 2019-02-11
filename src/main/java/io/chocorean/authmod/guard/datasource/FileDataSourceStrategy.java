package io.chocorean.authmod.guard.datasource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.exception.PlayerAlreadyExistException;
import io.chocorean.authmod.exception.RegistrationException;
import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;

public class FileDataSourceStrategy implements IDataSourceStrategy {

  private final File authFile;
  private final List<IPlayer> players;
  private long lastModification;
  private static final Logger LOGGER = AuthMod.LOGGER;
  private static final String SEPARATOR = ",";

  public FileDataSourceStrategy() {
    this.authFile = Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile();
    this.players = new ArrayList<>();
    this.readFile();
  }

  public FileDataSourceStrategy(File authFile) {
    this.authFile = authFile;
    this.players = new ArrayList<>();
    this.readFile();
  }

  private void readFile() {
    this.players.clear();
    try {
      boolean created = this.authFile.createNewFile();
      LOGGER.info((created ? "Create " : "Use ") + this.authFile.getAbsolutePath());
      try (BufferedReader bf = new BufferedReader(new FileReader(this.authFile))) {
        String line;
        while ((line = bf.readLine()) != null && line.trim().length() > 0) {
          if (!line.startsWith("#")) {
            String[] parts = line.trim().split(SEPARATOR);
            IPlayer p = new Player();
            p.setEmail(parts[0].trim());
            p.setUsername(parts[1].trim());
            p.setPassword(parts[2].trim());
            p.setBanned(Boolean.parseBoolean(parts[3].trim()));
            this.players.add(p);
          }
        }
        this.lastModification = this.authFile.lastModified();
      }
    } catch (IOException e) {
      LOGGER.catching(e);
    }
  }

  private void reloadFile() {
    if (lastModification != this.authFile.lastModified()) this.readFile();
  }

  @Override
  public IPlayer find(String email, String username) {
    this.reloadFile();
    if (email != null) {
      return this.players
          .stream()
          .filter(tmp -> tmp.getEmail().equals(email))
          .findFirst()
          .orElse(null);
    }
    if (username != null) {
      return this.players
          .stream()
          .filter(tmp -> tmp.getUsername().equals(username))
          .findFirst()
          .orElse(null);
    }
    return null;
  }

  @Override
  public boolean add(IPlayer player) throws RegistrationException {
    this.reloadFile();
    if (!this.exist(player)) {
      this.players.add(player);
      this.saveFile();
      return true;
    } else {
      throw new PlayerAlreadyExistException();
    }
  }

  @Override
  public boolean exist(IPlayer player) {
    this.reloadFile();
    IPlayer p =
        this.players
            .stream()
            .filter(tmp -> player.getEmail().equals(tmp.getEmail()) || player.getUsername().equals(tmp.getUsername()))
            .findFirst()
            .orElse(null);
    return p != null;
  }

  private void saveFile() {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.authFile))) {
      bw.write(String.join(SEPARATOR, "# email", " username", " hashed password", " is banned ?"));
      bw.newLine();
      for (IPlayer entry : this.players) {
        bw.write(String.join(SEPARATOR, entry.getEmail(), entry.getUsername(), entry.getPassword(), Boolean.toString(entry.isBanned())));
        bw.newLine();
      }
      this.lastModification = Files.getLastModifiedTime(this.authFile.toPath()).toMillis();
    } catch (IOException e) {
      LOGGER.catching(e);
    }
  }
}
