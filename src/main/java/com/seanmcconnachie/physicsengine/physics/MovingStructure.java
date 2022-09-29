package com.seanmcconnachie.physicsengine.physics;

import com.seanmcconnachie.physicsengine.simpledata.ThreeDoubles;
import com.seanmcconnachie.physicsengine.simpledata.ThreeFloats;
import com.seanmcconnachie.physicsengine.simpledata.ThreeInts;
import com.seanmcconnachie.physicsengine.simpledata.ThreeLongs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;

import java.util.Arrays;
import java.util.Objects;

public class MovingStructure extends MovementProperties implements MovementBase, java.io.Serializable {
    private final PointsStorage points;
    private final MovementData movementData;
    private ThreeLongs currAbsPos;
    float energyLoss;
    private ThreeLongs prevAbsPos;
    private boolean firstDraw = true;
    private boolean ignoreCollision = false;
    int i = 0;

    // Constructor
    public MovingStructure(
            Level worldLevel,
            Player owningPlayer,
            MovementData movementV,
            PointsStorage points,
            float energyLoss
    ) {
        super(worldLevel, owningPlayer, movementV);
        this.points = points;
        this.prevAbsPos = new ThreeLongs(
                movementV.x.getLastPos(),
                movementV.y.getLastPos(),
                movementV.z.getLastPos()
        );
        this.movementData = movementV;
        this.energyLoss = energyLoss;
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

    private void unDrawBlock(Level worldLevel, long x, long y, long z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        worldLevel.removeBlock(blockPos, false);
    }

    private int getBlockId(Level worldLevel, long x, long y, long z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return Block.getId(worldLevel.getBlockState(blockPos));
    }

    private void updateBlocks(Level worldLevel) {
        int[] collisions = new int[3];
        this.currAbsPos = new ThreeLongs(
                this.movementData.x.calculateAbsoluteDistance(0),
                this.movementData.y.calculateAbsoluteDistance(0),
                this.movementData.z.calculateAbsoluteDistance(0)
        );
        this.movementData.incrementAll();

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
                        int blockId = points.points[x][y][z];
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

                            if (blockId != -1) {
                                this.unDrawBlock(
                                        worldLevel,
                                        currAbsPos.x() + currRelPosMinus.x(),
                                        currAbsPos.y() + currRelPosMinus.y(),
                                        currAbsPos.z() + currRelPosMinus.z()
                                        );
                            }
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
                            if (!points.hasRotated && blockId != points.points
                                    [currRelPosAdd.x()]
                                    [currRelPosAdd.y()]
                                    [currRelPosAdd.z()]
                            ) {
                                this.drawBlock(
                                        worldLevel,
                                        currAbsPos.x() + x,
                                        currAbsPos.y() + y,
                                        currAbsPos.z() + z,
                                        blockId
                                );
                            } else if (points.hasRotated && blockId != points.prev_points
                                    [currRelPosAdd.x()]
                                    [currRelPosAdd.y()]
                                    [currRelPosAdd.z()]) {
                                this.drawBlock(
                                        worldLevel,
                                        currAbsPos.x() + x,
                                        currAbsPos.y() + y,
                                        currAbsPos.z() + z,
                                        blockId
                                );
                            }
                        } else {
                            if (blockId != -1) {
                                this.drawBlock(
                                        worldLevel,
                                        currAbsPos.x() + x,
                                        currAbsPos.y() + y,
                                        currAbsPos.z() + z,
                                        blockId
                                );
                            } else {
                                // Collision detection
                                if (this.getBlockId(
                                        worldLevel,
                                        this.currAbsPos.x() + x,
                                        this.currAbsPos.y() + y,
                                        this.currAbsPos.z() + z
                                ) != 0 && !this.ignoreCollision) {
                                    if (x < points.getArrayUpperSize()/2) {
                                        collisions[0] += 1;
                                    } else {
                                        collisions[0] += -1;
                                    }
                                    if (y < points.getArrayUpperSize()/2) {
                                        collisions[1] += 1;
                                    } else {
                                        collisions[1] += -1;
                                    }
                                    if (z < points.getArrayUpperSize()/2) {
                                        collisions[2] += 1;
                                    } else {
                                        collisions[2] += -1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this.ignoreCollision = false;

        if (collisions[0] != 0 || collisions[1] != 0 || collisions[2] != 0) {
            this.updateCollision(collisions);
        }

        this.points.hasRotated = false;
        this.firstDraw = false;
        this.prevAbsPos = currAbsPos;
        i++;
        if (i % (2*20) == 0) {
            points.rotatePoints(new ThreeFloats(180, 90, 90));
        }
    }

    private void updateCollision(int[] collisions) {
        // Find magnitude
        Vector collisionVector = new Vector(
                new double[][]{{
                        collisions[0],
                        collisions[1],
                        collisions[2]
                }}
        );

        Vector collisionUnitVector = collisionVector.normalize();

        Vector velocityVector = new Vector(
                new double[][]{{
                        this.movementData.x.calculateVelocity(0),
                        this.movementData.y.calculateVelocity(0),
                        this.movementData.z.calculateVelocity(0)
                }}
        );

        double velocityMagnitude = velocityVector.magnitude();
        velocityMagnitude *= this.energyLoss;

        ThreeDoubles newVelocities = new ThreeDoubles(
                collisionUnitVector.values[0][0] * velocityMagnitude,
                collisionUnitVector.values[0][1] * velocityMagnitude,
                collisionUnitVector.values[0][2] * velocityMagnitude
        );

        this.movementData.x.onImpact(prevAbsPos.x(), newVelocities.x());
        this.movementData.y.onImpact(prevAbsPos.y(), newVelocities.y());
        this.movementData.z.onImpact(prevAbsPos.z(), newVelocities.z());
        this.ignoreCollision = true;
    }
}
