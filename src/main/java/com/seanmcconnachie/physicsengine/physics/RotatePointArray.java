package com.seanmcconnachie.physicsengine.physics;

import com.seanmcconnachie.physicsengine.simpledata.ThreeFloats;

public class RotatePointArray {
    public static int[][][] rotatePoints(int[][][] original_points, ThreeFloats rotation) {
        if (rotation.x() == 0 && rotation.y() == 0 && rotation.z() == 0) {
            return original_points;
        }



        return new int[][][]{};
    }
}
