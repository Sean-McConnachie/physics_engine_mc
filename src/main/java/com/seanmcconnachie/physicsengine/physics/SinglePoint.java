package com.seanmcconnachie.physicsengine.physics;

import com.seanmcconnachie.physicsengine.simpledata.ThreeLongs;

public class SinglePoint implements java.io.Serializable {
    int blockId;
    int x;
    int y;
    int z;
    long prevAbsX;
    long prevAbsY;
    long prevAbsZ;
    public SinglePoint(int blockId, int x, int y, int z, ThreeLongs initialPos) {
        this.blockId = blockId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.prevAbsX = initialPos.x() + x;
        this.prevAbsY = initialPos.y() + y;
        this.prevAbsZ = initialPos.z() + z;
    }
}
