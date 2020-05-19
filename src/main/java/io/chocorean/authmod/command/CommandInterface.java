package io.chocorean.authmod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.chocorean.authmod.AuthMod;
import io.chocorean.authmod.core.GuardInterface;
import io.chocorean.authmod.core.PayloadInterface;
import io.chocorean.authmod.event.Handler;
import io.chocorean.authmod.util.text.ServerTranslationTextComponent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;

public interface CommandInterface extends Command<CommandSource> {

  public RequiredArgumentBuilder<CommandSource, String> getParameters();

}

