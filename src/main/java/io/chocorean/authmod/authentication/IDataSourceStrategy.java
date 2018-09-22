package io.chocorean.authmod.authentication;

import io.chocorean.authmod.model.IPlayer;

public interface IDataSourceStrategy {

    IPlayer retrieve(IPlayer player) throws Exception;

    IPlayer add(IPlayer player) throws Exception;
    
    boolean exist(IPlayer player);

}
