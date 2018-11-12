package io.chocorean.authmod.authentication.db;

import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayersDAO<P> implements IPlayersDAO<IPlayer> {

    private final String table;

    public PlayersDAO(String table) {
        this.table = table;
    }

    @Override
    public void create(IPlayer player) throws SQLException {
        Connection conn = ConnectionFactory.getConnection();
        String query = String.format("INSERT INTO %s(email, password, uuid, username) VALUES(?,?)", this.table);
        try(PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, player.getEmail());
            stmt.setString(2, player.getPassword());
            stmt.setString(3, player.getUuid());
            stmt.setString(4, player.getUsername());
            stmt.executeUpdate();
        }
        conn.close();
    }

    @Override
    public Player findById(int id) throws SQLException {
        Connection conn = ConnectionFactory.getConnection();
        try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT * FROM %s WHERE id = ?", this.table));
            ResultSet rs = stmt.executeQuery()) {
            stmt.setInt(1, id);
            conn.close();
            return createPlayer(rs);
        }
    }

    @Override
    public List<IPlayer> findAll() throws SQLException {
        Connection conn = ConnectionFactory.getConnection();
        List<IPlayer> players = new ArrayList<>();
        try(Statement stmt = conn.createStatement();
            ResultSet rs  = stmt.executeQuery(String.format("SELECT * FROM %s", this.table))) {
            while(rs.next())
                players.add(PlayersDAO.createPlayer(rs));
        }
        conn.close();
        return players;
    }

    @Override
    public Player findByEmail(String email) throws SQLException {
        Connection conn = ConnectionFactory.getConnection();
        ResultSet rs = null;
        try(PreparedStatement stmt = conn.prepareStatement(String.format("SELECT * FROM %s WHERE email = ?", this.table))) {
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            return createPlayer(rs);
        }
        finally {
            conn.close();
            if (rs != null) rs.close();
        }
    }

    @Override
    public IPlayer findFirst(IPlayer player) throws SQLException { Connection conn = ConnectionFactory.getConnection();
       ResultSet rs = null;
        try(PreparedStatement stmt = conn.prepareStatement(
                String.format("SELECT * FROM %s WHERE email = ? OR username = ?", this.table))) {
            stmt.setString(1, player.getEmail());
            stmt.setString(2, player.getUsername());
            rs = stmt.executeQuery();
            return createPlayer(rs);
        }
        finally {
            conn.close();
            if (rs != null) rs.close();
        }
    }

    private static Player createPlayer(ResultSet rs) throws SQLException {
        if(rs != null && rs.next()) {
            Player player = new Player();
            player.setBan(rs.getInt("isBan") != 0);
            player.setEmail(rs.getString("email"));
            player.setPassword(rs.getString("password"));
            player.setUsername(rs.getString("username"));
            player.setUuid(rs.getString("uuid"));
            return player;
        }
        else {
            return null;
        }
    }
}
