package com.seanmcconnachie.physicsengine.physics;

import com.seanmcconnachie.physicsengine.simpledata.ThreeLongs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;

import java.util.Objects;

public class MovingStructure extends MovementProperties implements MovementBase, java.io.Serializable {
    private final int[][][] blockIds;
    private ThreeLongs prevAbsPos;
    private boolean firstDraw = true;

    // Constructor
    public MovingStructure(
            Level worldLevel,
            Player owningPlayer,
            MovementVector movementV,
            int[][][] blockIds
    ) {
        super(worldLevel, owningPlayer, movementV);
        this.blockIds = blockIds;
        this.prevAbsPos = new ThreeLongs(
                movementV.x.getLastPos(),
                movementV.y.getLastPos(),
                movementV.z.getLastPos()
        );
    }

    @Override
    public void update(TickEvent.ServerTickEvent event) {
        if (this.getStop()) {
            return;
        }
        Level worldLevel;
        if (Objects.equals(this.getWorldLevel(), "minecraft:overworld")) {
            worldLevel = event.getServer().getLevel(Level.OVERWORLD);
        } else if (Objects.equals(this.getWorldLevel(), "minecraft:the_nether")) {
            worldLevel = event.getServer().getLevel(Level.NETHER);
        } else if (Objects.equals(this.getWorldLevel(), "minecraft:the_end")) {
            worldLevel = event.getServer().getLevel(Level.END);
        } else {
            return;
        }
        this.updateBlocks(worldLevel);
    }

    private void drawBlock(Level worldLevel, long x, long y, long z, int blockId) {
        if (blockId == -1) {
            this.unDrawBlock(worldLevel, x, y, z);
            return;
        }
        BlockPos blockPos = new BlockPos(x, y, z);
        BlockState block = Block.stateById(blockId);
        worldLevel.setBlockAndUpdate(blockPos, block);
    }

    private void unDrawBlock(Level worldLevel, long x, long y, long z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        worldLevel.removeBlock(blockPos, false);
    }

    private void updateBlocks(Level worldLevel) {
        final ThreeLongs currAbsPos = new ThreeLongs(
                this.movementVector.x.calculateAbsoluteDistance(),
                this.movementVector.y.calculateAbsoluteDistance(),
                this.movementVector.z.calculateAbsoluteDistance()
        );
        final ThreeLongs currRelIncreases = currAbsPos.subtract(this.prevAbsPos);

        for (int x = 0; x < this.blockIds.length; x++) {
            for (int y = 0; y < blockIds[x].length; y++) {
                for (int z = 0; z < blockIds[x][y].length; z++) {
                    if (firstDraw) {
                        this.drawBlock(
                                worldLevel,
                                currAbsPos.x() + x,
                                currAbsPos.y() + y,
                                currAbsPos.z() + z,
                                this.blockIds[x][y][z]
                        );
                    } else {
                        if (
                                x - currRelIncreases.x() < 0
                                        || x - currRelIncreases.x() >= this.blockIds.length
                                        || y - currRelIncreases.y() < 0
                                        || y - currRelIncreases.y() >= this.blockIds[x].length
                                        || z - currRelIncreases.z() < 0
                                        || z - currRelIncreases.z() >= this.blockIds[x][y].length
                        ) {
                            this.unDrawBlock(
                                    worldLevel,
                                    currAbsPos.x() + x - currRelIncreases.x(),
                                    currAbsPos.y() + y - currRelIncreases.y(),
                                    currAbsPos.z() + z - currRelIncreases.z()
                            );
                        }
                        if (
                                x < blockIds.length - currRelIncreases.x()
                                        && x + currRelIncreases.x() < blockIds.length
                                        && x + currRelIncreases.x() >= 0

                                        && y < blockIds[x].length - currRelIncreases.y()
                                        && y + currRelIncreases.y() < blockIds[x].length
                                        && y + currRelIncreases.y() >= 0

                                        && z < blockIds[x][y].length - currRelIncreases.z()
                                        && z + currRelIncreases.z() < blockIds[x][y].length
                                        && z + currRelIncreases.z() >= 0
                        ) {
                            if (blockIds[x][y][z] != blockIds
                                    [(int) (x + currRelIncreases.x())]
                                    [(int) (y + currRelIncreases.y())]
                                    [(int) (z + currRelIncreases.z())]
                            ) {
                                this.drawBlock(
                                        worldLevel,
                                        currAbsPos.x() + x,
                                        currAbsPos.y() + y,
                                        currAbsPos.z() + z,
                                        this.blockIds[x][y][z]
                                );
                            }
                        } else {
                            this.drawBlock(
                                    worldLevel,
                                    currAbsPos.x() + x,
                                    currAbsPos.y() + y,
                                    currAbsPos.z() + z,
                                    this.blockIds[x][y][z]
                            );
                        }
                    }
                }
            }
        }
        firstDraw = false;
        this.prevAbsPos = currAbsPos;
        this.movementVector.incrementAll();
    }
}