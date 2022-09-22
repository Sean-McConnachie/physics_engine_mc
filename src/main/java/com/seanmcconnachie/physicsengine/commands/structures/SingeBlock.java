package com.seanmcconnachie.physicsengine.commands.structures;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.seanmcconnachie.physicsengine.PhysicsEngine;
import com.seanmcconnachie.physicsengine.physics.MovementData;
import com.seanmcconnachie.physicsengine.physics.MovementVector;
import com.seanmcconnachie.physicsengine.physics.MovingStructure;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SingeBlock {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        // Creation commands ===========================================================================================
        dispatcher.register(Commands.literal("physics")
                .then(Commands.literal("block")
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .then(Commands.argument("item", ItemArgument.item(context))
                                        .executes((command) -> addPhysicsBlock(
                                                command.getSource(),
                                                BlockPosArgument.getLoadedBlockPos(command, "pos"),
                                                ItemArgument.getItem(command, "item"),
                                                new Vec3(0f, 0f, 0f),
                                                new Vec3(0f, -9.81f, 0f)
                                        ))
                                        .then(Commands.argument("velocity", Vec3Argument.vec3())
                                                .executes((command) -> addPhysicsBlock(
                                                        command.getSource(),
                                                        BlockPosArgument.getLoadedBlockPos(command, "pos"),
                                                        ItemArgument.getItem(command, "item"),
                                                        Vec3Argument.getVec3(command, "velocity"),
                                                        new Vec3(0f, -9.81f, 0f)
                                                ))
                                                .then(Commands.argument("acceleration", Vec3Argument.vec3())
                                                        .executes((command) -> addPhysicsBlock(
                                                                command.getSource(),
                                                                BlockPosArgument.getLoadedBlockPos(command, "pos"),
                                                                ItemArgument.getItem(command, "item"),
                                                                Vec3Argument.getVec3(command, "velocity"),
                                                                Vec3Argument.getVec3(command, "acceleration")
                                                        ))
                                                ))))));
    }

    private static int addPhysicsBlock(CommandSourceStack source, BlockPos pos, ItemInput item, Vec3 initialVels, Vec3 gravity) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        MovementVector movementV = new MovementVector(
                new MovementData(pos.getX(), initialVels.x(), gravity.x()),
                new MovementData(pos.getY(), initialVels.y(), gravity.y()),
                new MovementData(pos.getZ(), initialVels.z(), gravity.z())
        );
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
}
