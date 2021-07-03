package de.maxhenkel.gravestone.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.maxhenkel.corelib.death.Death;
import de.maxhenkel.corelib.death.DeathManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.UUIDArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DeathsCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> literalBuilder = Commands.literal("deaths").requires((commandSource) -> commandSource.hasPermission(2));

        Command<CommandSource> main = (commandSource) -> {
            ServerPlayerEntity player = EntityArgument.getPlayer(commandSource, "target");
            List<Death> deaths = DeathManager.getDeaths(player.getLevel(), player.getUUID());

            List<String> deathUUIDs = deaths.stream().map(death -> death.getId().toString()).collect(Collectors.toList());

            commandSource.getSource().sendSuccess(new StringTextComponent(String.join("\n", deathUUIDs)), true);
            return 1;
        };

        literalBuilder
            .then(Commands.argument("target", EntityArgument.player()).executes(main)
        );

        dispatcher.register(literalBuilder);
    };
}
