package io.chocorean.authmod.authentication;

import io.chocorean.authmod.authentication.datasource.FileDataSourceStrategy;
import io.chocorean.authmod.authentication.datasource.IDataSourceStrategy;
import io.chocorean.authmod.exception.LoginException;

public class Authenticator {

    private final IDataSourceStrategy dataSource;

    public Authenticator() {
        this(new FileDataSourceStrategy(null));
    }

    public Authenticator(IDataSourceStrategy dataSourceStrategy) {
        this.dataSource = dataSourceStrategy;
    }

    public boolean login(LoginPayload payload) throws LoginException {
        return false;
    }

    public IDataSourceStrategy getDataSourceStrategy() {
        return this.dataSource;
    }
}
