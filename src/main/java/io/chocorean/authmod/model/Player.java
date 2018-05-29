package io.chocorean.authmod.model;

public class Player implements IPlayer {

    private int id;
    private String password;
    private String email;
    private boolean isBan;
    private String uuid;
    private String username;

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isBan() {
        return isBan;
    }

    @Override
    public void setBan(boolean ban) {
        isBan = ban;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isPremium() { return this.getUuid() != null; }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return String.format("%3d | %s |", this.getId(), this.getEmail());
    }
}
