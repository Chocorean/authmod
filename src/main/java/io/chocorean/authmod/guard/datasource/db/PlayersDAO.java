package io.chocorean.authmod.guard.datasource.db;

import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;

import javax.validation.constraints.Email;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayersDAO implements IPlayersDAO<IPlayer> {

    private final String table;
    private final IConnectionFactory connectionFactory;
    private final Map<String, String> columns;
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String UUID = "uuid";
    private static final String PASSWORD = "password";
    private static final String BANNED = "banned";

    public PlayersDAO(IConnectionFactory connectionFactory) throws SQLException {
        this("players", connectionFactory);
    }

    public PlayersDAO(IConnectionFactory connectionFactory, Map<String, String> columns) throws SQLException {
        this("players", connectionFactory, columns);
    }

    public PlayersDAO(String table, IConnectionFactory connectionFactory, Map<String, String> columns) throws SQLException {
        this.table = table;
        this.columns = columns;
        this.connectionFactory = connectionFactory;
        this.checkTable();
    }

    public PlayersDAO(String table, IConnectionFactory connectionFactory) throws SQLException {
        this.table = table;
        this.connectionFactory = connectionFactory;
        this.columns = new HashMap<>();
        this.columns.put(EMAIL, EMAIL);
        this.columns.put(BANNED, BANNED);
        this.columns.put(PASSWORD, PASSWORD);
        this.columns.put(USERNAME, USERNAME);
        this.columns.put(UUID, UUID);
        this.checkTable();
    }

    private void checkTable() throws SQLException {
        try(
            Connection connection = this.connectionFactory.getConnection();
            PreparedStatement stmt = connection.prepareStatement(String.format("SELECT %s,%s,%s,%s,%s FROM %s",
                this.columns.getOrDefault(EMAIL, EMAIL),
                this.columns.getOrDefault(BANNED, BANNED),
                this.columns.getOrDefault(PASSWORD, PASSWORD),
                this.columns.getOrDefault(USERNAME, USERNAME),
                this.columns.getOrDefault(UUID, UUID),
                this.table))
        ) {
            stmt.executeQuery();
        }
    }

    @Override
    public void create(IPlayer player) throws SQLException {
        String query = String.format("INSERT INTO %s(%s, %s, %s, %s) VALUES(?, ?, ?, ?)",
            this.table,
            this.columns.get(EMAIL),
            this.columns.get(PASSWORD),
            this.columns.get(USERNAME),
            this.columns.get(UUID)
        );
        try(Connection conn = this.connectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, player.getEmail());
            stmt.setString(2, player.getPassword());
            stmt.setString(3, player.getUsername());
            if(player.getUuid() == null || player.getUuid().length() == 0)
                stmt.setNull(4, Types.VARCHAR);
            else
                stmt.setString(4, player.getUuid());
            stmt.executeUpdate();
        }
    }

    @Override
    public IPlayer find(IPlayer player) throws SQLException {
        try(Connection conn = this.connectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ? OR %s = ?",
                this.table,
                this.columns.get(EMAIL),
                this.columns.get(USERNAME)
            ))) {
            stmt.setString(1, player.getEmail());
            stmt.setString(2, player.getUsername());
            ResultSet rs = stmt.executeQuery();
            return createPlayer(rs);
        }
    }

    private Player createPlayer(ResultSet rs) throws SQLException {
        return createPlayer(rs, true);
    }

    private Player createPlayer(ResultSet rs, boolean closeAtEnd) throws SQLException {
        Player player = null;
        if(rs != null && rs.next()) {
            player = new Player();
            player.setBanned(rs.getInt(this.columns.get(BANNED)) != 0);
            player.setEmail(rs.getString(this.columns.get(EMAIL)));
            player.setPassword(rs.getString(this.columns.get(PASSWORD)));
            player.setUsername(rs.getString(this.columns.get(USERNAME)));
            player.setUuid(rs.getString(this.columns.get(UUID)));
        }
        if(closeAtEnd && rs != null)
            rs.close();
        return player;
    }

}
