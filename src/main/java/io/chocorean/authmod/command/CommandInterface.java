package io.chocorean.authmod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;

public interface CommandInterface extends Command<CommandSource> {

  RequiredArgumentBuilder<CommandSource, String> getParameters();

  LiteralArgumentBuilder<CommandSource> getCommandBuilder();
}

