package com.seanmcconnachie.physicsengine.physics;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class MovementProperties implements java.io.Serializable {
    private boolean Stop = false;
    private final String worldLevel;
    private final UUID owningPlayerId;

    public MovementVector movementVector;

    // Constructor
    public MovementProperties(
            Level worldLevel,
            Player owningPlayer,
            MovementVector movementV
    ) {
        this.worldLevel = worldLevel.dimension().location().toString();
        this.owningPlayerId = owningPlayer.getUUID();
        this.movementVector = movementV;
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
