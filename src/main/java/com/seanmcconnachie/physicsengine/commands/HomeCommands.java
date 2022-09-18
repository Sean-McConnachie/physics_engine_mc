package com.seanmcconnachie.physicsengine.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.seanmcconnachie.physicsengine.PhysicsEngine;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;


public class HomeCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("home")
                .then(Commands.literal("set")
                        .executes((command) -> setHome(command.getSource()))
                )
        );
        dispatcher.register(Commands.literal("home")
                .then(Commands.literal("return")
                        .executes((command) -> returnHome(command.getSource()))
                )
        );
    }

    private static int setHome(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        BlockPos playerPos = player.getOnPos();
        String pos = "(" + playerPos.getX() + ", " + playerPos.getY() + ", " + playerPos.getZ() + ")";

        player.getPersistentData().putIntArray(PhysicsEngine.MOD_ID + "homepos",
                new int[]{ playerPos.getX(), playerPos.getY(), playerPos.getZ() });

        source.sendSuccess(Component.literal("Set Home at " + pos), true);
        return 1;
    }

    private static int returnHome(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        boolean hasHomePos = player.getPersistentData().getIntArray(PhysicsEngine.MOD_ID + "homepos").length != 0;

        if(hasHomePos) {
            int[] playerPos = player.getPersistentData().getIntArray(PhysicsEngine.MOD_ID + "homepos");
            player.moveTo(playerPos[0], playerPos[1] + 1, playerPos[2]);

            source.sendSuccess(Component.literal("Player returned Home!"), true);
            return 1;
        } else {
            source.sendSuccess(Component.literal("No Home Position has been set!"), true);
            return -1;
        }
    }
}
