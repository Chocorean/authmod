package io.chocorean.authmod.authentication.db;

import io.chocorean.authmod.model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayersDAO implements IPlayersDAO<Player> {

    private final Connection connection;
    private final String table;

    public PlayersDAO(Connection connection) {
        this.connection = connection;
        this.table = "Players";
    }

    @Override
    public void create(Player player) throws SQLException {
        String query = String.format("INSERT INTO %s(email, password) VALUES(?,?)", this.table);
        try(PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setString(1, player.getEmail());
            stmt.setString(2, player.getPassword());
            stmt.executeUpdate();
        }
    }

    @Override
    public Player findById(int id) throws SQLException {
        try(PreparedStatement stmt = this.connection.prepareStatement(String.format("SELECT * FROM %s WHERE id = ?", this.table))) {
            stmt.setInt(1, id);
            ResultSet rs= stmt.executeQuery();
            return createPlayer(rs);
        }
    }

    @Override
    public List<Player> findAll() throws SQLException {
        List<Player> players = new ArrayList<>();
        try(Statement stmt = this.connection.createStatement()) {
            ResultSet rs  = stmt.executeQuery(String.format("SELECT * FROM %s", this.table));
            while(rs.next())
                players.add(PlayersDAO.createPlayer(rs));
            rs.close();
        }
        return players;
    }

    @Override
    public Player findByEmail(String email) throws SQLException {
        try(PreparedStatement stmt = this.connection.prepareStatement(String.format("SELECT * FROM %s WHERE email = ?", this.table))) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return createPlayer(rs);
        }
    }

    @Override
    public Player findByEmailOrUsername(String identifier) throws SQLException {
        try(PreparedStatement stmt = this.connection.prepareStatement(String.format("SELECT * FROM %s WHERE email = ? OR username = ?", this.table))) {
            stmt.setString(1, identifier);
            stmt.setString(2, identifier);
            ResultSet rs = stmt.executeQuery();
            return createPlayer(rs);
        }
    }

    private static Player createPlayer(ResultSet rs) throws SQLException {
        if(rs.next()) {
            Player player = new Player();
            player.setId(rs.getInt("id"));
            player.setBan(rs.getInt("isBan") != 0);
            player.setEmail(rs.getString("email"));
            player.setPassword(rs.getString("password"));
            player.setUsername(rs.getString("username"));
            player.setUuid(rs.getString("uuid"));
            return player;
        }
        else
            return null;
    }
}
