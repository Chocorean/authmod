package io.chocorean.authmod.core.datasource;

public interface PasswordHashInterface {

  String hash(String data);

  boolean check(String hashedPassword, String password);

}
