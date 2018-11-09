package io.chocorean.authmod.authentication;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.authentication.datasource.IDataSourceStrategy;
import io.chocorean.authmod.exception.*;
import io.chocorean.authmod.model.IPlayer;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthModule implements IAuthenticationStrategy {

    private static final Logger LOGGER = FMLLog.log;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final IDataSourceStrategy strategy;

    public AuthModule(IDataSourceStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public IPlayer login(IPlayer player) throws AuthmodException {
        IPlayer saved = this.strategy.retrieve(player);
        LOGGER.info("[AuthMod]: " + player + " trying to login");
        if(saved == null)
            throw new PlayerNotFoundException(AuthMod.getConfig().getPlayerNotFoundMsg());
        if(!saved.getUsername().equals(player.getUsername()))
            throw new DifferentUsernameException(AuthMod.getConfig().getWrongUsernameMsg());
        if(saved.isBan())
            throw new BanException(AuthMod.getConfig().getBannedMsg());
        boolean correctPassword = BCrypt.checkpw(player.getPassword(), saved.getPassword());
        if(!correctPassword) {
            throw new WrongPasswordException(AuthMod.getConfig().getWrongPasswordMsg());
        }
        return player;
    }

    @Override
    public IPlayer register(IPlayer player) throws AuthmodException {
        LOGGER.info("[AuthMod]: " + player + " trying to register");
        String hostedDomain = AuthMod.getConfig().getHostedDomain();
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(player.getEmail());
        if(!matcher.find()) {
            throw new InvalidEmailException(AuthMod.getConfig().getNotValidEmailMsg());
        }
        if(hostedDomain.length() > 0 && !player.getEmail().endsWith(hostedDomain)) {
            throw new UnauthorizedHostedDomainException();
        }
        if(this.strategy.exist(player)) {
            throw new PlayerAlreadyExistException(AuthMod.getConfig().getPlayerAlreadyExistsMsg());
        }
        player.setPassword(BCrypt.hashpw(player.getPassword(), BCrypt.gensalt()));
        this.strategy.add(player);
        return player;
    }

    @Override
    public boolean exist(IPlayer player) {
        return this.strategy.exist(player);
    }
}
