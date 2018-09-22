package io.chocorean.authmod.authentication;

import io.chocorean.authmod.exception.PlayerAlreadyExistException;
import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileDataSourceStrategy implements IDataSourceStrategy {

    private final File authFile;
    private Map<String, IPlayer> players;
    private static final Logger LOGGER = FMLLog.getLogger();
    private static final String SEPARATOR = ";";

    public FileDataSourceStrategy(File authFile) {
        this.authFile = authFile;
        this.players = new HashMap<>();
        try {
            this.readFile();
        } catch (IOException e) {
            LOGGER.catching(e);
        }
    }

    private void readFile() throws IOException {
        this.authFile.createNewFile();
        BufferedReader bf = new BufferedReader(new FileReader(this.authFile));
        String line;
        while((line = bf.readLine()) != null) {
            String[] parts = line.split(SEPARATOR);
            IPlayer p = new Player();
            p.setEmail(parts[0]);
            p.setUsername(parts[1]);
            p.setPassword(parts[2]);
            p.setBan(Boolean.parseBoolean(parts[3]));
            this.players.put(p.getEmail(), p);
        }
        bf.close();
    }

    @Override
    public IPlayer retrieve(IPlayer player) {
        return this.players.get(player.getEmail());
    }

    @Override
    public IPlayer add(IPlayer player) throws Exception {
        this.players.put(player.getEmail(), player);
        this.saveFile();
        return player;
    }

    @Override
    public boolean exist(IPlayer player) {
        IPlayer p = this.players.values().stream()
                .filter(tmp -> player.getEmail().equals(tmp.getEmail()) || player.getUsername().equals(tmp.getUsername()))
                .findFirst().orElse(null);
        return p != null;
    }

    private void saveFile() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(this.authFile));
        for(String email: this.players.keySet())
            bw.write(String.join(SEPARATOR,
                    email,
                    this.players.get(email).getUsername(),
                    this.players.get(email).getPassword(),
                    Boolean.toString(this.players.get(email).isBan()))
                    + "\n");
        bw.close();
    }

}
