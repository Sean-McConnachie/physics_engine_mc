package com.seanmcconnachie.physicsengine.data;

public class ThreeLongs implements java.io.Serializable {
    private final long x;
    private final long y;
    private final long z;

    public ThreeLongs(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getX() {
        return this.x;
    }

    public long getY() {
        return this.y;
    }

    public long getZ() {
        return this.z;
    }

    public ThreeLongs subtract(ThreeLongs other) {
        return new ThreeLongs(
                this.x - other.x,
                this.y - other.y,
                this.z - other.z
        );
    }

    public ThreeLongs add(ThreeLongs other) {
        return new ThreeLongs(
                this.x + other.x,
                this.y + other.y,
                this.z + other.z
        );
    }
}
