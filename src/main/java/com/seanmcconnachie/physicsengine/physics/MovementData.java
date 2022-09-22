package com.seanmcconnachie.physicsengine.physics;

public class MovementData implements java.io.Serializable {
    public MovementDataAxis x;
    public MovementDataAxis y;
    public MovementDataAxis z;

    // Constructor
    public MovementData(MovementDataAxis x, MovementDataAxis y, MovementDataAxis z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void incrementAll() {
        this.x.incrementTick();
        this.y.incrementTick();
        this.z.incrementTick();
    }
}
