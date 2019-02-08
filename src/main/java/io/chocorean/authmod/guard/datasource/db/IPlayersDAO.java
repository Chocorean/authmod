package io.chocorean.authmod.guard.datasource.db;

import io.chocorean.authmod.model.IPlayer;

import java.sql.SQLException;
import java.util.List;

public interface IPlayersDAO<P extends IPlayer> {

    void create(P player) throws SQLException;

    P findById(int id) throws SQLException;

    List<P> findAll() throws SQLException;

    P findByEmail(String email) throws SQLException;

    P findFirst(P player) throws SQLException;
}
