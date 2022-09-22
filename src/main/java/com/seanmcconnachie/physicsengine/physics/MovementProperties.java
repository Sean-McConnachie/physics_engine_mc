package com.seanmcconnachie.physicsengine.physics;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class MovementProperties implements java.io.Serializable {
    private boolean Stop = false;
    private final String worldLevel;
    private final UUID owningPlayerId;

    public MovementData movementData;

    // Constructor
    public MovementProperties(
            Level worldLevel,
            Player owningPlayer,
            MovementData movementV
    ) {
        this.worldLevel = worldLevel.dimension().location().toString();
        this.owningPlayerId = owningPlayer.getUUID();
        this.movementData = movementV;
    }

    // Getters and Setters =============================================================================================

    public String getWorldLevel() {
        return this.worldLevel;
    }

    public UUID getOwningPlayerId() {
        return this.owningPlayerId;
    }

    public boolean getStop() {
        return this.Stop;
    }

    public void setStop(boolean stop) {
        this.Stop = stop;
    }
}
