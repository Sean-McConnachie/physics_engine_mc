package com.seanmcconnachie.physicsengine.physics;

public class MovementVector implements java.io.Serializable {
    public MovementData x;
    public MovementData y;
    public MovementData z;

    // Constructor
    public MovementVector(MovementData x, MovementData y, MovementData z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
