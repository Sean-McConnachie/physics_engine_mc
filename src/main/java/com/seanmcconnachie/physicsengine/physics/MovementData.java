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

    public double[] calculateLengthAndDirection(int relativeTick) {
        double[] result = new double[4];
        double x_vel = this.x.calculateVelocity(relativeTick);
        double y_vel = this.y.calculateVelocity(relativeTick);
        double z_vel = this.z.calculateVelocity(relativeTick);
        result[0] = Math.sqrt(Math.pow(x_vel, 2) + Math.pow(y_vel, 2) + Math.pow(z_vel, 2));
        result[1] = Math.acos(x_vel/result[0]);
        result[2] = Math.acos(y_vel/result[0]);
        result[3] = Math.acos(z_vel/result[0]);
        return result;
    }
}
