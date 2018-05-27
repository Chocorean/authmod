package io.chocorean.authmod.db;

import io.chocorean.authmod.model.Player;

import java.sql.SQLException;
import java.util.List;

public interface IPlayersDAO {

    Player findById(int id) throws SQLException;

    List<Player> findAll() throws SQLException;

    Player findByEmail(String email) throws SQLException;

    Player findByEmailOrUsername(String identifier) throws SQLException;
}
