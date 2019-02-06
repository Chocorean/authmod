package io.chocorean.authmod.authentication;

import io.chocorean.authmod.authentication.datasource.IDataSourceStrategy;
import io.chocorean.authmod.exception.BannedPlayerException;
import io.chocorean.authmod.exception.DifferentUsernameException;
import io.chocorean.authmod.exception.LoginException;
import io.chocorean.authmod.exception.PlayerNotFoundException;
import io.chocorean.authmod.model.IPlayer;
import org.mindrot.jbcrypt.BCrypt;

public class Authenticator {

    private final IDataSourceStrategy dataSource;

    public Authenticator(IDataSourceStrategy dataSourceStrategy) {
        this.dataSource = dataSourceStrategy;
    }

    public boolean login(LoginPayload payload) throws LoginException {
        if(payload != null) {
            if(payload.isValid()) {
                IPlayer player = this.dataSource.find(payload.getEmail(), payload.getUsername());
                if(player == null)
                    throw new PlayerNotFoundException();
                if(!player.getUsername().equals(payload.getUsername()))
                    throw new DifferentUsernameException();
                if(player.isBanned())
                    throw new BannedPlayerException();
                boolean correctPassword = BCrypt.checkpw(player.getPassword(), payload.getPassword());
                return correctPassword;
            }
        }
        return false;
    }

    public IDataSourceStrategy getDataSourceStrategy() {
        return this.dataSource;
    }
}
