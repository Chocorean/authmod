package io.chocorean.authmod.authentication;

public interface IPayload {
    public IPayload setPassword(String password);
    public boolean isValid();
    public IPayload setUsername(String username);
    public IPayload setUuid(String uuid);
    public String getUuid();
    public IPayload setEmail(String email);
    public String getEmail();
    public String getUsername();
    public String getPassword();
}
