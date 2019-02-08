package io.chocorean.authmod.guard.datasource;

import io.chocorean.authmod.exception.RegistrationException;
import io.chocorean.authmod.model.IPlayer;

public interface IDataSourceStrategy {

    IPlayer find(String email, String username);

    boolean add(IPlayer player) throws RegistrationException;
    
    boolean exist(IPlayer player);

}
