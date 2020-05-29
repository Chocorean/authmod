package io.chocorean.authmod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.chocorean.authmod.core.Payload;
import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.core.Player;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Arrays;
import java.util.Objects;

public interface CommandInterface extends Command<CommandSource> {

  RequiredArgumentBuilder<CommandSource, String> getParameters();

  LiteralArgumentBuilder<CommandSource> getCommandBuilder();

  static PayloadInterface toPayload(PlayerEntity entity, String ...args) {
    // TODO when offline, UUID is not the mojang UUID
    String uuid = PlayerEntity.getUUID(entity.getGameProfile()).toString();
    return new Payload(new Player(entity.getDisplayName().getString(), uuid),
      Arrays.stream(args).filter(Objects::nonNull).toArray(String[]::new));
  }

}

