package io.chocorean.authmod.authentication;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.authentication.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.authentication.datasource.IDataSourceStrategy;
import io.chocorean.authmod.exception.*;
import io.chocorean.authmod.model.IPlayer;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;

public class Registrator {

    private static final Logger LOGGER = AuthMod.LOGGER;
    private final IDataSourceStrategy dataSource;

    public Registrator() {
        this(new FileDataSourceStrategy(null));
    }

    public Registrator(IDataSourceStrategy dataSourceStrategy) {
        this.dataSource = dataSourceStrategy;
    }

    public boolean register(RegistrationPayload payload) throws RegistrationException {
        if(payload.isValid()) {
            LOGGER.info(payload.getUsername() + " is registering");
            IPlayer player = PlayerFactory.createFromRegistrationPayload(payload);
            player.setPassword(BCrypt.hashpw(player.getPassword(), BCrypt.gensalt()));
            return false;
        }
        return false;
    }

    public IDataSourceStrategy getDataSourceStrategy() {
        return this.dataSource;
    }
}
