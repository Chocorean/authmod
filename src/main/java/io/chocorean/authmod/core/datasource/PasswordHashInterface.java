package io.chocorean.authmod.core.datasource;

public interface PasswordHashInterface {

  public String hash(String data);

  public boolean check(String hashedPassword, String password);

}
