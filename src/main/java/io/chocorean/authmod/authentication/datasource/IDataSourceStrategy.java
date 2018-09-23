package io.chocorean.authmod.authentication.datasource;

import io.chocorean.authmod.exception.AuthmodException;
import io.chocorean.authmod.model.IPlayer;

public interface IDataSourceStrategy {

    IPlayer retrieve(IPlayer player) throws AuthmodException;

    IPlayer add(IPlayer player) throws AuthmodException;
    
    boolean exist(IPlayer player);

}
