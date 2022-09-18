package com.seanmcconnachie.physicsengine.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.seanmcconnachie.physicsengine.PhysicsEngine;
import com.seanmcconnachie.physicsengine.data.ThreeDoubles;
import com.seanmcconnachie.physicsengine.physics.MovementData;
import com.seanmcconnachie.physicsengine.physics.MovementVector;
import com.seanmcconnachie.physicsengine.physics.MovingStructure;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;


public class PhysicsCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        // Creation commands ===========================================================================================
        dispatcher.register(Commands.literal("physics")
                .then(Commands.literal("static_block")
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                .then(Commands.argument("item", ItemArgument.item(context))
                .executes((command) -> placeBlock(
                        command.getSource(),
                        BlockPosArgument.getLoadedBlockPos(command, "pos"),
                        ItemArgument.getItem(command, "item")
        ))))));

//        dispatcher.register(Commands.literal("physics")
//                .then(Commands.literal("block")
//                .then(Commands.argument("pos", BlockPosArgument.blockPos())
//                .then(Commands.argument("item", ItemArgument.item(context))
//                .then(Commands.argument("initial_v", Vec3Argument.vec3())
//                .then(Commands.argument("gravity", Vec3Argument.vec3())
//                .executes((command) -> addPhysicsBlock(
//                        command.getSource(),
//                        BlockPosArgument.getLoadedBlockPos(command, "pos"),
//                        ItemArgument.getItem(command, "item"),
//                        Vec3Argument.getVec3(command, "gravity"),
//                        Vec3Argument.getVec3(command, "initial_v")
//        ))))))));

        dispatcher.register(Commands.literal("physics")
                .then(Commands.literal("block")
                    .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .then(Commands.argument("item", ItemArgument.item(context))
                                .executes((command) -> addPhysicsBlock(
                                        command.getSource(),
                                        BlockPosArgument.getLoadedBlockPos(command, "pos"),
                                        ItemArgument.getItem(command, "item"),
                                        new ThreeDoubles(0, 0, 0),
                                        new ThreeDoubles(0, -9.81f, 0)
                                ))
                            .then(Commands.argument("Xv", DoubleArgumentType.doubleArg())
                                .then(Commands.argument("Yv", DoubleArgumentType.doubleArg())
                                    .then(Commands.argument("Zv", DoubleArgumentType.doubleArg())
                                        .executes((command) -> addPhysicsBlock(
                                            command.getSource(),
                                            BlockPosArgument.getLoadedBlockPos(command, "pos"),
                                            ItemArgument.getItem(command, "item"),
                                            new ThreeDoubles(
                                                DoubleArgumentType.getDouble(command, "Xv"),
                                                DoubleArgumentType.getDouble(command, "Yv"),
                                                DoubleArgumentType.getDouble(command, "Zv")
                                            ),
                                            new ThreeDoubles(0, -9.81f, 0)
                                            ))
                                        .then(Commands.argument("Xg", DoubleArgumentType.doubleArg())
                                            .then(Commands.argument("Yg", DoubleArgumentType.doubleArg())
                                                .then(Commands.argument("Zg", DoubleArgumentType.doubleArg())
                                                    .executes((command) -> addPhysicsBlock(
                                                        command.getSource(),
                                                        BlockPosArgument.getLoadedBlockPos(command, "pos"),
                                                        ItemArgument.getItem(command, "item"),
                                                        new ThreeDoubles(
                                                            DoubleArgumentType.getDouble(command, "Xv"),
                                                            DoubleArgumentType.getDouble(command, "Yv"),
                                                            DoubleArgumentType.getDouble(command, "Zv")
                                                        ),
                                                        new ThreeDoubles(
                                                            DoubleArgumentType.getDouble(command, "Xg"),
                                                            DoubleArgumentType.getDouble(command, "Yg"),
                                                            DoubleArgumentType.getDouble(command, "Zg")
                                                        )))))))))))));

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
    private static int placeBlock(CommandSourceStack source, BlockPos pos, ItemInput item) throws CommandSyntaxException, NullPointerException {
        ServerPlayer player = source.getPlayerOrException();
        // Place item in world at given location
        // Create default block state from ItemInput
        BlockState blockState = ((BlockItem) item.getItem()).getBlock().defaultBlockState();
        // Create block entity from block state
        player.level.setBlock(pos, blockState, 3);

        source.sendSuccess(Component.literal("Dropping " + blockState + "at (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")"), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int addPhysicsBlock(CommandSourceStack source, BlockPos pos, ItemInput item, ThreeDoubles initialVels, ThreeDoubles gravity) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        MovementData xMovement = new MovementData(pos.getX(), initialVels.getX(), gravity.getX());
        MovementData yMovement = new MovementData(pos.getY(), initialVels.getY(), gravity.getY());
        MovementData zMovement = new MovementData(pos.getZ(), initialVels.getZ(), gravity.getZ());

        MovementVector movementV = new MovementVector(xMovement, yMovement, zMovement);
        BlockState blockState = ((BlockItem) item.getItem()).getBlock().defaultBlockState();
        int blockId = Block.getId(blockState);
        int[][][] blockIds = new int[][][]{{{blockId}}};

        MovingStructure structure = new MovingStructure(
                player.level,
                player,
                movementV,
                blockIds);
        PhysicsEngine.movingStructures.add(structure);

        System.out.println("Created and dropping structure.");
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
}
