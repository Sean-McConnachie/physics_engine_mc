package com.seanmcconnachie.physicsengine.commands.structures;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.seanmcconnachie.physicsengine.PhysicsEngine;
import com.seanmcconnachie.physicsengine.simpledata.ThreeFloats;
import com.seanmcconnachie.physicsengine.physics.MovementData;
import com.seanmcconnachie.physicsengine.physics.MovementVector;
import com.seanmcconnachie.physicsengine.physics.MovingStructure;
import com.seanmcconnachie.physicsengine.physics.RotatePointArray;
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

public class BoxNotCustomizable {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        // Creation commands ===========================================================================================
        dispatcher.register(Commands.literal("physics")
                .then(Commands.literal("box")
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .then(Commands.argument("item1", ItemArgument.item(context))
                                        .then(Commands.argument("item2", ItemArgument.item(context))
                                                .executes((command) -> addPhysicsBox(
                                                        command.getSource(),
                                                        BlockPosArgument.getLoadedBlockPos(command, "pos"),
                                                        ItemArgument.getItem(command, "item1"),
                                                        ItemArgument.getItem(command, "item2"),
                                                        new Vec3(0f, 0f, 0f),
                                                        new Vec3(0f, -9.81f, 0f),
                                                        new Vec3(0f, 0f, 0f)
                                                ))
                                                .then(Commands.argument("velocity", Vec3Argument.vec3())
                                                        .then(Commands.argument("acceleration", Vec3Argument.vec3())
                                                                .executes((command) -> addPhysicsBox(
                                                                        command.getSource(),
                                                                        BlockPosArgument.getLoadedBlockPos(command, "pos"),
                                                                        ItemArgument.getItem(command, "item1"),
                                                                        ItemArgument.getItem(command, "item2"),
                                                                        Vec3Argument.getVec3(command, "velocity"),
                                                                        Vec3Argument.getVec3(command, "acceleration"),
                                                                        new Vec3(0f, 0f, 0f)
                                                                ))
                                                                .then(Commands.argument("rotation", Vec3Argument.vec3())
                                                                        .executes((command) -> addPhysicsBox(
                                                                                command.getSource(),
                                                                                BlockPosArgument.getLoadedBlockPos(command, "pos"),
                                                                                ItemArgument.getItem(command, "item1"),
                                                                                ItemArgument.getItem(command, "item2"),
                                                                                Vec3Argument.getVec3(command, "velocity"),
                                                                                Vec3Argument.getVec3(command, "acceleration"),
                                                                                Vec3Argument.getVec3(command, "rotation")
                                                                        ))
                                                        ))))))));
    }

    private static int addPhysicsBox(
            CommandSourceStack source,
            BlockPos pos,
            ItemInput item1,
            ItemInput item2,
            Vec3 initialVelsVec,
            Vec3 gravityVec,
            Vec3 rotationVec
    ) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        ThreeFloats rotation = new ThreeFloats((float) rotationVec.x(), (float) rotationVec.y(), (float) rotationVec.z());
        MovementVector movementV = new MovementVector(
                new MovementData(pos.getX(), initialVelsVec.x(), gravityVec.x()),
                new MovementData(pos.getY(), initialVelsVec.y(), gravityVec.y()),
                new MovementData(pos.getZ(), initialVelsVec.z(), gravityVec.z())
        );
        BlockState blockState1 = ((BlockItem) item1.getItem()).getBlock().defaultBlockState();
        BlockState blockState2 = ((BlockItem) item2.getItem()).getBlock().defaultBlockState();
        int blockId1 = Block.getId(blockState1);
        int blockId2 = Block.getId(blockState2);
        int[][][] blockIds = new int[][][]{
                {{blockId1, blockId2, blockId1}, {blockId1, -1, blockId2}, {blockId1, blockId2, blockId2}},
                {{blockId1, -1, blockId1}, {-1, -1, -1}, {blockId2, -1, blockId1}},
                {{blockId2, blockId1, blockId1}, {blockId2, -1, blockId1}, {blockId2, blockId2, blockId2}}
        };

        blockIds = RotatePointArray.rotatePoints(blockIds, rotation);

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
