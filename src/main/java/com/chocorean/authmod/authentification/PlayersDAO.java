package com.chocorean.authmod.authentification;

import com.chocorean.authmod.db.ConnectionFactory;
import com.chocorean.authmod.models.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayersDAO implements IPlayersDAO {

    private final Connection connection;
    private String table;

    public PlayersDAO(Connection connection) {
        this.connection = connection;
        this.table = "Players";
    }

    @Override
    public Player findById(int id) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement stmt = connection.prepareStatement(String.format("SELECT * FROM %s WHERE id = ?", this.table));
        stmt.setInt(1, id);
        ResultSet rs= stmt.executeQuery();
        while(rs.next())
            return createPlayer(rs);
        return null;
    }

    @Override
    public List<Player> findAll() throws SQLException {
        List<Player> players = new ArrayList<>();
        Connection connection = ConnectionFactory.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs  = stmt.executeQuery(String.format("SELECT * FROM %s", this.table));
        while(rs.next())
            players.add(PlayersDAO.createPlayer(rs));
        return players;
    }

    @Override
    public Player findByEmail(String email) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement stmt = connection.prepareStatement(String.format("SELECT * FROM %s WHERE email = ?", this.table));
        stmt.setString(1, email);
        ResultSet rs= stmt.executeQuery();
        while(rs.next())
            return createPlayer(rs);
        return null;
    }

    public static Player createPlayer(ResultSet rs) throws SQLException {
        Player player = new Player();
        player.setId(rs.getInt("id"));
        player.setAdmin(rs.getInt("isAdmin") != 0);
        player.setBan(rs.getInt("isBan") != 0);
        player.setAvatar(rs.getString("avatar"));
        player.setEmail(rs.getString("email"));
        player.setFirstname(rs.getString("firstName"));
        player.setLastname(rs.getString("lastName"));
        player.setLastConnection(rs.getDate("lastConnection"));
        player.setPassword(rs.getString("password"));
        player.setUsername(rs.getString("username"));
        player.setUuid(rs.getString("uuid"));
        return player;
    }
}
