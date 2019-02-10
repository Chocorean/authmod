package io.chocorean.authmod.guard.datasource.db;

import io.chocorean.authmod.model.IPlayer;
import java.sql.SQLException;

public interface IPlayersDAO<P extends IPlayer> {

  void create(P player) throws SQLException;

  P find(P player) throws SQLException;
}
