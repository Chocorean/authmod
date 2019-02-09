package io.chocorean.authmod.guard.datasource.db;

import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayersDAO implements IPlayersDAO<IPlayer> {

    private final String table;
    private final IConnectionFactory connectionFactory;
    private final Map<String, String> columns;

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
        this(table, connectionFactory, new HashMap<String, String>() {{
            put("email", "email");
            put("banned", "banned");
            put("password", "password");
            put("username", "username");
            put("uuid", "uuid");
        }});
    }

    private void checkTable() throws SQLException {
        try(
                Connection connection = this.connectionFactory.getConnection();
                PreparedStatement stmt = connection.prepareStatement(String.format("SELECT %s,%s,%s,%s,%s FROM %s",
                this.columns.getOrDefault("email", "email"),
                this.columns.getOrDefault("banned", "banned"),
                this.columns.getOrDefault("password", "password"),
                this.columns.getOrDefault("username", "username"),
                this.columns.getOrDefault("uuid", "uuid"),
                this.table))
        ) {
            stmt.executeQuery();
        }
    }

    @Override
    public void create(IPlayer player) throws SQLException {
        String query = String.format("INSERT INTO %s(%s, %s, %s, %s) VALUES(?, ?, ?, ?)",
                this.table,
                this.columns.get("email"),
                this.columns.get("password"),
                this.columns.get("username"),
                this.columns.get("uuid")
        );
        try(Connection conn = this.connectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, player.getEmail());
            stmt.setString(2, player.getPassword());
            stmt.setString(3, player.getUsername());
            if(player.getUuid() == null)
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
                    this.columns.get("email"),
                    this.columns.get("username")
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
            player.setBanned(rs.getInt(this.columns.get("banned")) != 0);
            player.setEmail(rs.getString(this.columns.get("email")));
            player.setPassword(rs.getString(this.columns.get("password")));
            player.setUsername(rs.getString(this.columns.get("username")));
            player.setUuid(rs.getString(this.columns.get("uuid")));
        }
        if(closeAtEnd && rs != null)
            rs.close();
        return player;
    }

}
