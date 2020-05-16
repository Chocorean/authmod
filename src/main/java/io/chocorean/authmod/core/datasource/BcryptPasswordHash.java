package io.chocorean.authmod.core.datasource;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptPasswordHash implements PasswordHashInterface {

  public String hash(String data) {
    return BCrypt.hashpw(data, BCrypt.gensalt());
  }

  @Override
  public boolean check(String hashedPassword, String password) {
    try {
      if (hashedPassword == null || password == null)
        return false;
      if(hashedPassword.equals(""))
        return false;
      return BCrypt.checkpw(password, hashedPassword);
    } catch(Exception e) {
      return false;
    }
  }

}
