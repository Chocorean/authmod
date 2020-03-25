package io.chocorean.authmod.core;


public class Player implements PlayerInterface {

  private String uuid;

  private String username;

  public Player() {
    this.uuid = "";
    this.username = "";
  }

  public Player(String username, String uuid) {
    this.setUuid(uuid);
    this.setUsername(username);
  }

  public String getUuid() {
    return uuid;
  }

  public Player setUuid(String uuid) {
    if (uuid == null) {
      this.uuid = "";
    } else {
      if (uuid.length() == 32) {
        uuid =
          String.format(
            "%s-%s-%s-%s-%s",
            uuid.substring(0, 8),
            uuid.substring(8, 12),
            uuid.substring(12, 16),
            uuid.substring(16, 20),
            uuid.substring(20, 32));
      }
      this.uuid = uuid.length() == 36 ? uuid : "";
    }
    return this;
  }

  public String getUsername() {
    return username;
  }

  public Player setUsername(String username) {
    this.username = username == null ? "" : username.trim();
    return this;
  }

  public boolean isPremium() {
    return !this.getUuid().equals("");
  }

  public String toString() {
    return String.format("{username: %s, uuid: %s}", this.getUsername(), this.getUuid());
  }

}
