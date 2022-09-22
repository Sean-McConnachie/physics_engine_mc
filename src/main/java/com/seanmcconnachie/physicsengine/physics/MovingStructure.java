package com.seanmcconnachie.physicsengine.physics;

import com.seanmcconnachie.physicsengine.simpledata.ThreeInts;
import com.seanmcconnachie.physicsengine.simpledata.ThreeLongs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovingStructure extends MovementProperties implements MovementBase, java.io.Serializable {
    private final PointsStorage points;
    private final MovementData movementData;
    private ThreeLongs prevAbsPos;
    private boolean firstDraw = true;

    // Constructor
    public MovingStructure(
            Level worldLevel,
            Player owningPlayer,
            MovementData movementV,
            PointsStorage points
    ) {
        super(worldLevel, owningPlayer, movementV);
        this.points = points;
        this.prevAbsPos = new ThreeLongs(
                movementV.x.getLastPos(),
                movementV.y.getLastPos(),
                movementV.z.getLastPos()
        );
        this.movementData = movementV;
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
        BlockPos blockPos = new BlockPos(x, y, z);
        BlockState block = Block.stateById(blockId);
        worldLevel.setBlockAndUpdate(blockPos, block);
    }

    private void unDrawBlock(Level worldLevel, long x, long y, long z, int blockId) {
        BlockPos blockPos = new BlockPos(x, y, z);
        worldLevel.removeBlock(blockPos, false);
    }

    private int getBlockId(Level worldLevel, long x, long y, long z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return Block.getId(worldLevel.getBlockState(blockPos));
    }

    private void updateBlocks(Level worldLevel) {
        final ThreeLongs currAbsPos = new ThreeLongs(
                this.movementData.x.calculateAbsoluteDistance(),
                this.movementData.y.calculateAbsoluteDistance(),
                this.movementData.z.calculateAbsoluteDistance()
        );

        ThreeLongs currRelIncreases = currAbsPos.subtract(this.prevAbsPos);

        for (int x = 0; x < points.points.length; x++) {
            for (int y = 0; y < points.points[x].length; y++) {
                for (int z = 0; z < points.points[x][y].length; z++) {
                    if (firstDraw) {
                        if (points.points[x][y][z] == -1) {
                            continue;
                        }
                        this.drawBlock(
                                worldLevel,
                                currAbsPos.x() + x,
                                currAbsPos.y() + y,
                                currAbsPos.z() + z,
                                points.points[x][y][z]
                        );
                    } else {
                        ThreeInts currRelPosMinus = new ThreeInts(
                                (int) (x - currRelIncreases.x()),
                                (int) (y - currRelIncreases.y()),
                                (int) (z - currRelIncreases.z())
                        );

                        if (
                                currRelPosMinus.x() < 0
                                        || currRelPosMinus.x() >= points.points.length
                                        || currRelPosMinus.y() < 0
                                        || currRelPosMinus.y() >= points.points[x].length
                                        || currRelPosMinus.z() < 0
                                        || currRelPosMinus.z() >= points.points[x][y].length
                        ) {
                            this.unDrawBlock(
                                    worldLevel,
                                    currAbsPos.x() + currRelPosMinus.x(),
                                    currAbsPos.y() + currRelPosMinus.y(),
                                    currAbsPos.z() + currRelPosMinus.z(),
                                    0
                            );
                        }
                        ThreeInts currRelPosAdd = new ThreeInts(
                                (int) (x + currRelIncreases.x()),
                                (int) (y + currRelIncreases.y()),
                                (int) (z + currRelIncreases.z())
                        );
                        if (
                                x < points.points.length - currRelIncreases.x()
                                        && currRelPosAdd.x() < points.points.length
                                        && currRelPosAdd.x() >= 0

                                        && y < points.points[x].length - currRelIncreases.y()
                                        && currRelPosAdd.y() < points.points[x].length
                                        && currRelPosAdd.y() >= 0

                                        && z < points.points[x][y].length - currRelIncreases.z()
                                        && currRelPosAdd.z() < points.points[x][y].length
                                        && currRelPosAdd.z() >= 0
                        ) {
                            if (points.points[x][y][z] != points.points
                                    [currRelPosAdd.x()]
                                    [currRelPosAdd.y()]
                                    [currRelPosAdd.z()]
                            ) {
                                this.drawBlock(
                                        worldLevel,
                                        currAbsPos.x() + x,
                                        currAbsPos.y() + y,
                                        currAbsPos.z() + z,
                                        points.points[x][y][z]
                                );
                            }
                        } else {
                            this.drawBlock(
                                    worldLevel,
                                    currAbsPos.x() + x,
                                    currAbsPos.y() + y,
                                    currAbsPos.z() + z,
                                    points.points[x][y][z]
                            );
                        }
                    }
                }
            }
        }
        firstDraw = false;
        this.prevAbsPos = currAbsPos;
        this.movementData.incrementAll();
    }
}
