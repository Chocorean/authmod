package io.chocorean.authmod.authentication;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.authentication.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.authentication.datasource.IDataSourceStrategy;
import io.chocorean.authmod.exception.AuthmodException;
import io.chocorean.authmod.exception.PlayerAlreadyExistException;
import io.chocorean.authmod.model.IPlayer;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import javax.validation.ConstraintViolation;
import java.nio.file.Paths;
import java.util.Set;

public class Registrator {

    private static final Logger LOGGER = AuthMod.LOGGER;
    private final IDataSourceStrategy dataSource;

    public Registrator() {
        this(new FileDataSourceStrategy(Paths.get(System.getProperty("java.io.tmpdir"), "authmod.csv").toFile()));
    }

    public Registrator(IDataSourceStrategy dataSourceStrategy) {
        this.dataSource = dataSourceStrategy;
    }

    public boolean register(RegistrationPayload payload) throws AuthmodException {
        if(payload != null) {
            if(payload.isValid()) {
                IPlayer player = PlayerFactory.createFromRegistrationPayload(payload);
                if(this.dataSource.exist(player)) {
                    throw new PlayerAlreadyExistException();
                } else {
                    LOGGER.info(payload.getUsername() + " is registering");
                    player.setPassword(BCrypt.hashpw(player.getPassword(), BCrypt.gensalt()));
                    return this.dataSource.add(player);
                }
            } else {
                Set<ConstraintViolation<IPayload>> errors = payload.getErrors();
                for(ConstraintViolation c: errors) {
                    MappingConstraintViolationsToExceptions.throwException(c);
                }
            }
        }
        return false;
    }

    public IDataSourceStrategy getDataSourceStrategy() {
        return this.dataSource;
    }

}
