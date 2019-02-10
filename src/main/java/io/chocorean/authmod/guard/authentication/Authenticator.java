package io.chocorean.authmod.guard.authentication;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.guard.datasource.IDataSourceStrategy;
import io.chocorean.authmod.exception.BannedPlayerException;
import io.chocorean.authmod.exception.DifferentUsernameException;
import io.chocorean.authmod.exception.LoginException;
import io.chocorean.authmod.exception.PlayerNotFoundException;
import io.chocorean.authmod.guard.payload.LoginPayload;
import io.chocorean.authmod.model.IPlayer;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class Authenticator {

    private static final Logger LOGGER = AuthMod.LOGGER;
    private final IDataSourceStrategy dataSource;

    public Authenticator(IDataSourceStrategy dataSourceStrategy) {
        this.dataSource = dataSourceStrategy;
    }

    public boolean login(LoginPayload payload) throws LoginException {
        if(payload != null && payload.isValid()) {
            IPlayer player = this.dataSource.find(payload.getEmail(), payload.getUsername());
            if(player == null)
                throw new PlayerNotFoundException();
            if(!player.getUsername().equals(payload.getUsername()))
                throw new DifferentUsernameException();
            if(player.isBanned())
                throw new BannedPlayerException();
            LOGGER.info(payload.getUsername() + " is signin in");
            return BCrypt.checkpw(payload.getPassword(), player.getPassword());
        }
        return false;
    }

    public IDataSourceStrategy getDataSourceStrategy() {
        return this.dataSource;
    }
}
