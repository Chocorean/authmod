package com.chocorean.authmod.authentification;

import com.chocorean.authmod.PlayerNotFoundException;
import com.chocorean.authmod.db.ConnectionFactory;
import com.chocorean.authmod.models.Player;
import org.mindrot.jbcrypt.BCrypt;

public class DatabaseAuthenticationStrategy implements IAuthenticationStrategy {

    private IPlayersDAO playersDAO;

    public DatabaseAuthenticationStrategy() {
        this.playersDAO = new PlayersDAO(ConnectionFactory.getConnection());
    }

    @Override
    public boolean login(String user, String password) throws Exception {
        Player p = this.playersDAO.findByEmail(user);
        if(p == null)
            throw new PlayerNotFoundException(String.format("%s is not registered", user));
        return BCrypt.checkpw(password, p.getPassword());
    }
}
