package com.chocorean.authmod.authentification;

import com.chocorean.authmod.exceptions.LoginException;

public interface IAuthenticationStrategy {

    boolean login(String user, String password) throws LoginException;

}
