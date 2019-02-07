package io.chocorean.authmod.model;

public interface IPlayer {

    boolean isBanned();

    IPlayer setBanned(boolean ban);

    IPlayer setPassword(String password);

    String getPassword();

    IPlayer setEmail(String email);

    String getEmail();

    IPlayer setUsername(String username);

    String getUsername();

    boolean isPremium();

    IPlayer setUuid(String uuid);

    String getUuid();
}
