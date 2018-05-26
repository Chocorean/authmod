package com.chocorean.authmod.authentification;

public interface IAuthenticationStrategy {

    public boolean login(String user, String password) throws Exception;

}
