package io.chocorean.authmod.model;

import io.chocorean.authmod.AuthMod;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Player implements IPlayer {

    private String password;

    @Email
    private String email;

    private boolean banned;

    @Size(min = 36, max = 36)
    private String uuid;

    @NotNull
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
        this.email = email == null ? "" : email.trim();
    }

    @Override
    public boolean isBanned() {
        return banned;
    }

    @Override
    public void setBanned(boolean ban) {
        banned = ban;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        if(uuid == null) {
            this.uuid = "";
        } else {
            if(uuid.length() == 32) {
                uuid = String.format("%s-%s-%s-%s-%s",
                        uuid.substring(0, 8),
                        uuid.substring(8, 12),
                        uuid.substring(12, 16),
                        uuid.substring(16, 20),
                        uuid.substring(20, 32)
                );
            }
            this.uuid = uuid.length() == 36 ? uuid : "";
        }
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isPremium() { return this.getUuid().length() != 0; }

    @Override
    public void setUsername(String username) {
        this.username = username == null ? "" : username.trim();
    }

    public String toString() {
        return String.format("{%s, %s}", this.getEmail(), this.getUsername());
    }


}
