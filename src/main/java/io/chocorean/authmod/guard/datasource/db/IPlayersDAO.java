package io.chocorean.authmod.guard.datasource.db;

import java.sql.SQLException;

import io.chocorean.authmod.model.IPlayer;

public interface IPlayersDAO<P extends IPlayer> {

  void create(P player) throws SQLException;

  P find(P player) throws SQLException;
  
  boolean remove(P player) throws SQLException;
}
