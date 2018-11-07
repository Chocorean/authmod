package io.chocorean.authmod.authentication.datasource;

import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileDataSourceStrategy implements IDataSourceStrategy {

    private final File authFile;
    private final Map<String, IPlayer> players;
    private long lastModification;
    private static final Logger LOGGER = FMLLog.log;
    private static final String SEPARATOR = ",";

    public FileDataSourceStrategy(File authFile) {
        this.authFile = authFile;
        this.players = new HashMap<>();
        this.readFile();
    }

    private void readFile() {
        this.players.clear();
        try {
            this.authFile.createNewFile();
        } catch (IOException e) { LOGGER.catching(e); }
        try(BufferedReader bf = new BufferedReader(new FileReader(this.authFile))) {
            String line;
            while((line = bf.readLine()) != null && line.trim().length() > 0) {
                if(!line.startsWith("#")) {
                    String[] parts = line.trim().split(SEPARATOR);
                    IPlayer p = new Player();
                    p.setEmail(parts[0].trim());
                    p.setUsername(parts[1].trim());
                    p.setPassword(parts[2].trim());
                    p.setBan(Boolean.parseBoolean(parts[3].trim()));
                    this.players.put(p.getEmail(), p);
                }
            }
            this.lastModification = this.authFile.lastModified();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reloadFile() {
        if (lastModification != this.authFile.lastModified())
            this.readFile();
    }

    @Override
    public IPlayer retrieve(IPlayer player) {
        this.reloadFile();
        return this.players.get(player.getEmail());
    }

    @Override
    public IPlayer add(IPlayer player) {
        this.reloadFile();
        this.players.put(player.getEmail(), player);
        this.saveFile();
        return player;
    }

    @Override
    public boolean exist(IPlayer player) {
        this.reloadFile();
        IPlayer p = this.players.values().stream()
                .filter(tmp -> player.getEmail().equals(tmp.getEmail()) || player.getUsername().equals(tmp.getUsername()))
                .findFirst().orElse(null);
        return p != null;
    }

    private void saveFile() {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(this.authFile))) {
            bw.write(String.join(SEPARATOR,
                    "# email",
                    " username",
                    " hashed password",
                    " is banned ?"));
            bw.newLine();
            for(Map.Entry<String, IPlayer> entry: this.players.entrySet()) {
                bw.write(String.join(SEPARATOR,
                        entry.getKey(),
                        entry.getValue().getUsername(),
                        entry.getValue().getPassword(),
                        Boolean.toString(entry.getValue().isBan())));
                bw.newLine();
            }
        } catch (IOException e) { LOGGER.catching(e); }
    }

}
