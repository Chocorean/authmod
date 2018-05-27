package com.chocorean.authmod.db;

import com.chocorean.authmod.models.Player;
import java.sql.SQLException;
import java.util.List;

public interface IPlayersDAO {

    Player findById(int id) throws SQLException;

    List<Player> findAll() throws SQLException;

    Player findByEmail(String email) throws SQLException;
}
