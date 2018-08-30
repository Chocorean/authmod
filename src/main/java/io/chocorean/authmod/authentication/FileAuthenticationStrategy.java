package io.chocorean.authmod.authentication;

import io.chocorean.authmod.exception.PlayerAlreadyExistException;
import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileAuthenticationStrategy implements IAuthenticationStrategy {

    private final File authFile;
    private Map<String, IPlayer> players;
    private static final Logger LOGGER = FMLLog.getLogger();
    private static final String SEPARATOR = ";";

    public FileAuthenticationStrategy(File authFile) {
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
            this.players.put(p.getEmail(), p);
        }
        bf.close();
    }

    @Override
    public IPlayer login(IPlayer player) throws Exception {
        IPlayer check = this.players.get(player.getEmail());
        if(check == null) {
            IPlayer finalPlayer = player;
            check = this.players.values().stream().filter(p -> p.getUsername().equals(finalPlayer.getUsername()))
                    .findFirst()
                    .orElse(null);
        }
        player = AuthUtils.verifyAuthentication(check, player);
        return player;
    }

    @Override
    public IPlayer register(IPlayer player) throws Exception {
        if(this.players.containsKey(player.getEmail())) {
            throw new PlayerAlreadyExistException(player.getEmail() + " already exists!");
        } else {
            player = AuthUtils.Register(player);
            this.players.put(player.getEmail(), player);
            this.saveFile();
            return player;
        }
    }

    private void saveFile() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(this.authFile));
        for(String email: this.players.keySet())
            bw.write(String.format("%s%s%s%s%s\n", email, SEPARATOR, this.players.get(email).getUsername(), SEPARATOR, this.players.get(email).getPassword()));
        bw.close();
    }

}
