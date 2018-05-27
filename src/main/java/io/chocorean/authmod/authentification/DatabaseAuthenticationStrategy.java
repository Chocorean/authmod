package io.chocorean.authmod.authentification;

import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.db.ConnectionFactory;
import io.chocorean.authmod.db.IPlayersDAO;
import io.chocorean.authmod.db.PlayersDAO;
import io.chocorean.authmod.exception.BanException;
import io.chocorean.authmod.exception.LoginException;
import io.chocorean.authmod.exception.PlayerNotFoundException;
import io.chocorean.authmod.exception.WrongPasswordException;
import io.chocorean.authmod.model.Player;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class DatabaseAuthenticationStrategy implements IAuthenticationStrategy {

    private final IPlayersDAO playersDAO;
    private static final Logger LOGGER = FMLLog.getLogger();

    public DatabaseAuthenticationStrategy() {
        this.playersDAO = new PlayersDAO(ConnectionFactory.getConnection());
    }

    @Override
    public boolean login(MinecraftServer server, ICommandSender sender, String[] args) throws Exception {
        if(args.length != 2) {
            sender.addChatMessage(new TextComponentString("Invalid number of arguments: <email or username> <password>"));
        } else {
            Player p;
            try {
                p = this.playersDAO.findByEmailOrUsername(args[0]);
            } catch(SQLException e) {
                LOGGER.catching(Level.ERROR, e);
                throw new LoginException("Authentication is unavailable for the moment. Please contact " + AuthMod.config.getContact());
            }
            if(p == null)
                throw new PlayerNotFoundException(String.format("%s doesn't exist", args[0]));
            if(p.isBan())
                throw new BanException(String.format("Your account is  banned (%s), please contact %s.", args[0], AuthMod.config.getContact()));
            boolean correctPassword =  BCrypt.checkpw(args[1], p.getPassword());
            if(!correctPassword)
                throw new WrongPasswordException("Wrong password, please retry");
            return correctPassword;
        }
        return false;
    }


}
