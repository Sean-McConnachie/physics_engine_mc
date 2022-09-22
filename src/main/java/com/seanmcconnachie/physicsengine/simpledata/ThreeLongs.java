package com.seanmcconnachie.physicsengine.simpledata;

public record ThreeLongs(long x, long y, long z) implements java.io.Serializable {

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
