package com.seanmcconnachie.physicsengine.commands;

import com.seanmcconnachie.physicsengine.PhysicsEngine;
import com.seanmcconnachie.physicsengine.commands.structures.BoxNotCustomizable;
import com.seanmcconnachie.physicsengine.commands.structures.SingeBlock;
import com.seanmcconnachie.physicsengine.physics.MovingStructure;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.UUID;


public class PhysicsCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        // Creation commands ===========================================================================================
        SingeBlock.register(dispatcher, context);
        BoxNotCustomizable.register(dispatcher, context);

        // Pause commands ==============================================================================================
        dispatcher.register(Commands.literal("physics")
                .then(Commands.literal("pause")
                        .then(Commands.literal("all")
                                .executes((command) -> setStops(command.getSource(), true, true))

                        )));

        dispatcher.register(Commands.literal("physics")
                .then(Commands.literal("pause")
                        .then(Commands.literal("mine")
                                .executes((command) -> setStops(command.getSource(), false, true))
                        )));

        // Resume commands =============================================================================================
        dispatcher.register(Commands.literal("physics")
                .then(Commands.literal("resume")
                        .then(Commands.literal("all")
                                .executes((command) -> setStops(command.getSource(), true, false))

                        )));

        dispatcher.register(Commands.literal("physics")
                .then(Commands.literal("resume")
                        .then(Commands.literal("mine")
                                .executes((command) -> setStops(command.getSource(), false, false))
                        )));

        // Delete commands =============================================================================================
        dispatcher.register(Commands.literal("physics")
                .then(Commands.literal("delete")
                        .then(Commands.literal("all")
                                .executes((command) -> deleteStructures(command.getSource(), true))

                        )));

        dispatcher.register(Commands.literal("physics")
                .then(Commands.literal("delete")
                        .then(Commands.literal("mine")
                                .executes((command) -> deleteStructures(command.getSource(), false))
                        )));
    }

    private static int deleteStructures(CommandSourceStack source, boolean changeAll) throws CommandSyntaxException {
        UUID playerId = source.getPlayerOrException().getUUID();
        for (int counter = PhysicsEngine.movingStructures.size() - 1; counter >= 0; counter--) {
            MovingStructure structure = PhysicsEngine.movingStructures.get(counter);
            if (changeAll || structure.getOwningPlayerId() == playerId) {
                PhysicsEngine.movingStructures.remove(counter);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int setStops(CommandSourceStack source, boolean changeAll, boolean stop) throws CommandSyntaxException {
        UUID playerId = source.getPlayerOrException().getUUID();
        for (MovingStructure structure : PhysicsEngine.movingStructures) {
            if (changeAll || structure.getOwningPlayerId() == playerId) {
                structure.setStop(stop);
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
