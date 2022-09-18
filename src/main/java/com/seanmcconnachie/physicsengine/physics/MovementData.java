package com.seanmcconnachie.physicsengine.physics;

public class MovementData implements java.io.Serializable {
    private long lastPos;
    private int Tick;
    private double initialVelocity;
    private double Acceleration;

    // Constructor
    public MovementData(int initialPos, double initialVelocity, double acceleration) {
        this.lastPos = initialPos;
        this.Tick = 0;
        this.initialVelocity = initialVelocity;
        this.Acceleration = acceleration;
    }

    private int calculateRelativeDistance() {
        // s = ut + 1/2at^2
        // ticks -> seconds = tick * 0.05
        return (int) Math.round((this.initialVelocity * this.Tick * 0.05) + (0.5 * this.Acceleration * Math.pow(this.Tick * 0.05, 2)));
    }

    public long calculateAbsoluteDistance() {
        return this.lastPos + this.calculateRelativeDistance();
    }

    // Getters and Setters =============================================================================================
    public void incrementTick() {
        this.Tick++;
    }

    public void onImpact(int newPos) {
        this.lastPos = newPos;
        this.Tick = 0;
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
