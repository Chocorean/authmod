package io.chocorean.authmod.core.datasource;

import io.chocorean.authmod.core.Player;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

public class FileDataSourceStrategy implements DataSourceStrategyInterface {

  private static final String SEPARATOR = ",";
  private final File file;
  private final PasswordHashInterface passwordHash;
  private long lastModification;
  private final ArrayList<DataSourcePlayerInterface> players;

  public FileDataSourceStrategy(File file,  PasswordHashInterface passwordHash) throws IOException {
    this.file = file;
    this.passwordHash = passwordHash;
    this.players = new ArrayList<>();
    this.readFile();
  }

  public FileDataSourceStrategy(File file) throws IOException {
    this(file, new BcryptPasswordHash());
  }

  @Override
  public DataSourcePlayerInterface find(String identifier) {
    try {
      this.reloadFile();
      if (identifier != null) {
        return this.players
          .stream()
          .filter(tmp -> tmp.getIdentifier().equals(identifier))
          .findFirst()
          .orElse(null);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public boolean add(DataSourcePlayerInterface player) {
    if(!this.exist(player)) {
      this.players.add(player);
      this.saveFile();
      return true;
    }
    return false;
  }

  @Override
  public boolean exist(DataSourcePlayerInterface player) {
    return this.find(player.getIdentifier()) != null;
  }

  @Override
  public PasswordHashInterface getHashPassword() {
    return this.passwordHash;
  }

  private void saveFile() {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.file, true))) {
      bw.write(String.join(SEPARATOR, "# Identifier", " username", " hashed password", " is banned ?"));
      bw.newLine();
      for (DataSourcePlayerInterface entry : this.players) {
        bw.write(String.join(SEPARATOR, entry.getIdentifier(), entry.getUsername(), entry.getPassword(), Boolean.toString(entry.isBanned())));
        bw.newLine();
      }
      this.lastModification = Files.getLastModifiedTime(this.file.toPath()).toMillis();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readFile() throws IOException {
    this.players.clear();
    this.file.createNewFile();
    try (BufferedReader bf = new BufferedReader(new FileReader(this.file))) {
      String line;
      while ((line = bf.readLine()) != null && line.trim().length() > 0) {
        if (!line.trim().startsWith("#")) {
          String[] parts = line.trim().split(SEPARATOR);
          if(parts.length == 4) {
            DataSourcePlayerInterface p = new DataSourcePlayer(new Player());
            p.setIdentifier(parts[0].trim());
            p.setUsername(parts[1].trim());
            p.setPassword(parts[2]);
            p.setBanned(Boolean.parseBoolean(parts[3].trim()));
            this.players.add(p);
          }
        }
      }
      this.lastModification = this.file.lastModified();
    }
  }

  private void reloadFile() throws IOException {
    if (lastModification != this.file.lastModified()) this.readFile();
  }

}
