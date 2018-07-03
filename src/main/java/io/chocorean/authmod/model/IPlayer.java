package io.chocorean.authmod.model;

public interface IPlayer {

    boolean isBan();

    void setBan(boolean ban);

    void setPassword(String password);

    String getPassword();

    void setEmail(String email);

    String getEmail();

    void setUsername(String username);

    String getUsername();

    boolean isPremium();

    void setUuid(String uuid);

    String getUuid();
}
