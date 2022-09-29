package com.seanmcconnachie.physicsengine.physics;

public class MovementDataAxis implements java.io.Serializable {
    private long lastPos;
    private int Tick;
    private double initialVelocity;
    private double Acceleration;

    // Constructor
    public MovementDataAxis(int initialPos, double initialVelocity, double acceleration) {
        this.lastPos = initialPos;
        this.Tick = 0;
        this.initialVelocity = initialVelocity;
        this.Acceleration = acceleration;
    }

    public int calculateRelativeDistance(int relativeTick) {
        int currTick = this.Tick + relativeTick;
        // s = ut + 1/2at^2
        // ticks -> seconds = tick * 0.05
        return (int) Math.round((this.initialVelocity * currTick * 0.05) + (0.5 * this.Acceleration * Math.pow(currTick * 0.05, 2)));
    }

    public double calculateVelocity(int relativeTick) {
        int currTick = this.Tick + relativeTick;
        // u = v + atz
        return this.initialVelocity + (this.Acceleration * currTick * 0.05);
    }

    public long calculateAbsoluteDistance(int relativeTick) {
        return this.lastPos + this.calculateRelativeDistance(relativeTick);
    }

    // Getters and Setters =============================================================================================
    public void incrementTick() {
        this.Tick++;
    }

    public void onImpact(long newPos, double newVelocity) {
        this.lastPos = newPos;
        this.Tick = 0;
        this.initialVelocity = newVelocity;
    }

    public long getLastPos() {
        return this.lastPos;
    }

    public double getInitialVelocity() {
        return this.initialVelocity;
    }

    public void setInitialVelocity(float initialVelocity) {
        this.initialVelocity = initialVelocity;
    }

    public double getAcceleration() {
        return this.Acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.Acceleration = acceleration;
    }
}
