package com.seanmcconnachie.physicsengine.physics;

import com.seanmcconnachie.physicsengine.simpledata.ThreeFloats;

import java.util.Arrays;

public class PointsStorage implements java.io.Serializable {
    public int[][][] points;
    private int arrayUpperSize;

    public PointsStorage(int[][][] points) {
        this.points = padPoints(points);
    }

    private int[][][] padPoints(int[][][] points) {
        int x_length = points.length;
        int y_length = points[0].length;
        int z_length = points[0][0].length;

        int max_array_length = Math.max(x_length, Math.max(y_length, z_length));
        this.arrayUpperSize = (int) Math.ceil(Math.sqrt(Math.pow(max_array_length, 2) + Math.pow(max_array_length, 2) + Math.pow(max_array_length, 2)));
        if (this.arrayUpperSize % 2 == 1) {
            this.arrayUpperSize += 1;
        }
        int[][][] new_points = new int[this.arrayUpperSize][this.arrayUpperSize][this.arrayUpperSize];
        for (int i = 0; i < this.arrayUpperSize; i++) {
            for (int j = 0; j < this.arrayUpperSize; j++) {
                for (int k = 0; k < this.arrayUpperSize; k++) {
                    new_points[i][j][k] = -1;
                }
            }
        }
        int x_offset = (this.arrayUpperSize - x_length) / 2;
        int y_offset = (this.arrayUpperSize - y_length) / 2;
        int z_offset = (this.arrayUpperSize - z_length) / 2;
        for (int x = 0; x < x_length; x++) {
            for (int y = 0; y < y_length; y++) {
                System.arraycopy(points[x][y], 0, new_points[x + x_offset][y + y_offset], z_offset, z_length);
            }
        }
        return new_points;
    }

    public static int[][][] rotatePoints(int[][][] original_points, ThreeFloats rotation) {
        if (rotation.x() == 0 && rotation.y() == 0 && rotation.z() == 0) {
            return original_points;
        }

        return new int[][][]{};
    }
}
