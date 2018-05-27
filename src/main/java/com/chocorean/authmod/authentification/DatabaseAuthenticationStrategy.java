package com.chocorean.authmod.authentification;

import com.chocorean.authmod.db.ConnectionFactory;
import com.chocorean.authmod.db.IPlayersDAO;
import com.chocorean.authmod.db.PlayersDAO;
import com.chocorean.authmod.exceptions.LoginException;
import com.chocorean.authmod.exceptions.PlayerNotFoundException;
import com.chocorean.authmod.models.Player;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

public class DatabaseAuthenticationStrategy implements IAuthenticationStrategy {

    private final IPlayersDAO playersDAO;
    public static final Logger LOGGER = FMLLog.getLogger();

    public DatabaseAuthenticationStrategy() {
        this.playersDAO = new PlayersDAO(ConnectionFactory.getConnection());
    }

    @Override
    public boolean login(String user, String password) throws LoginException {
        Player p = null;
        try {
            p = this.playersDAO.findByEmail(user);
        } catch(SQLException e) {
            LOGGER.catching(Level.ERROR, e);
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            FMLLog.log(Level.ERROR, sw.toString());
        }
        if(p == null)
            throw new PlayerNotFoundException(String.format("%s is not registered", user));
        return BCrypt.checkpw(password, p.getPassword());
    }
}
