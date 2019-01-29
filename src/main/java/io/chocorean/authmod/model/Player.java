package io.chocorean.authmod.model;

public class Player implements IPlayer {

    private String password;
    private String email;
    private boolean isBanned;
    private String uuid;
    private String username;

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
        if(email != null)
        this.email = email.trim();
    }

    @Override
    public boolean isBanned() {
        return isBanned;
    }

    @Override
    public void setBanned(boolean ban) {
        isBanned = ban;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        if(uuid != null)
            this.uuid = uuid.trim();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isPremium() { return this.getUuid() != null; }

    @Override
    public void setUsername(String username) {
        this.username = username.trim();
    }

    public String toString() {
        return String.format("{%s, %s}", this.getEmail(), this.getUsername());
    }
}
