package io.chocorean.authmod.authentication;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.exception.*;
import io.chocorean.authmod.model.IPlayer;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthModule implements IAuthenticationStrategy {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private final IDataSourceStrategy strategy;

    public AuthModule(IDataSourceStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public IPlayer login(IPlayer player) throws Exception {
        IPlayer saved = this.strategy.retrieve(player);
        if(player == null || saved == null)
            throw new PlayerNotFoundException(String.format("%s doesn't exist", player.getEmail()));
        if(!saved.getUsername().equals(player.getUsername()))
            throw new DifferentUsernameException(String.format("Your username should be %s instead of %s. Please change it to login.", saved.getUsername(), player.getUsername()));
        if(saved.isBan())
            throw new BanException(String.format("Your account is  banned (%s), please contact %s.", player.getEmail(), AuthMod.getConfig().getContact()));
        boolean correctPassword = BCrypt.checkpw(player.getPassword(), saved.getPassword());
        if(!correctPassword) {
            throw new WrongPasswordException("Wrong password, please retry");
        }
        return player;
    }

    @Override
    public IPlayer register(IPlayer player) throws Exception {
        String hostedDomain = AuthMod.getConfig().getHostedDomain();
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(player.getEmail());
        if(!matcher.find()) {
            throw new InvalidEmailException(String.format("%s is not a valid email. Try again.", player.getEmail()));
        }
        if(hostedDomain.length() > 0 && !player.getEmail().endsWith(hostedDomain)) {
            throw new UnauthorizedHostedDomainException();
        }
        if(this.strategy.exist(player)) {
            throw new PlayerAlreadyExistException(player.getEmail() + " already exists!");
        }
        player.setPassword(BCrypt.hashpw(player.getPassword(), BCrypt.gensalt()));
        return player;
    }

    @Override
    public boolean exist(IPlayer player) {
        return this.strategy.exist(player);
    }
}
