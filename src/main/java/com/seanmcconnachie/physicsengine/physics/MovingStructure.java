package com.seanmcconnachie.physicsengine.physics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.TickEvent;

import java.util.Objects;

public class MovingStructure extends MovementProperties implements MovementBase, java.io.Serializable {
    private int[][][] previousBlockIds;
    private final int[][][] blockIds;


    // Constructor
    public MovingStructure(
            Level worldLevel,
            Player owningPlayer,
            MovementVector movementV,
            int[][][] blockIds
    ) {
        super(worldLevel, owningPlayer, movementV);
        this.blockIds = blockIds;
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
        // Player owningPlayer = event.getServer().getPlayerList().getPlayer(this.getOwningPlayerId());
    }

    private void updateBlocks(Level worldLevel) {
        final double xIncrease = this.movementVector.x.calculateAbsoluteDistance();
        final double yIncrease = this.movementVector.y.calculateAbsoluteDistance();
        final double zIncrease = this.movementVector.z.calculateAbsoluteDistance();

        for (int x = 0; x < this.blockIds.length; x++) {
            for (int y = 0; y < blockIds[x].length; y++) {
                for (int z = 0; z < blockIds[x][y].length; z++) {
                    BlockPos pos = new BlockPos(
                            x + xIncrease,
                            y + yIncrease,
                            z + zIncrease
                    );
                    this.movementVector.x.incrementTick();
                    this.movementVector.y.incrementTick();
                    this.movementVector.z.incrementTick();
                    int blockId = this.blockIds[x][y][z];
                    worldLevel.setBlockAndUpdate(pos, Block.stateById(blockId));
                }
            }
        }
    }
}
