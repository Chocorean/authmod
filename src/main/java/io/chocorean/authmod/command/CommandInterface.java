package io.chocorean.authmod.command;

import io.chocorean.authmod.core.Payload;
import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.core.Player;
import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.Objects;

public interface CommandInterface extends ICommand {

  static PayloadInterface toPayload(EntityPlayer entity, String... args) {
    String uuid = entity.getUniqueID().toString();
    return new Payload(
      new Player(entity.getDisplayNameString(), uuid),
      Arrays.stream(args).filter(Objects::nonNull).toArray(String[]::new)
    );
  }
}
