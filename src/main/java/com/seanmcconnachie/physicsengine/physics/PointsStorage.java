package com.seanmcconnachie.physicsengine.physics;

import com.seanmcconnachie.physicsengine.simpledata.ThreeFloats;
import com.seanmcconnachie.physicsengine.simpledata.ThreeInts;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;

public class PointsStorage implements java.io.Serializable {
    public int[][][] points;
    public int[][][] prev_points;
    public boolean hasRotated = false;
    private int arrayUpperSize;
    private int midPoint;

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
        this.midPoint = this.arrayUpperSize / 2;
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

    public void rotatePoints(ThreeFloats rotation) {
        rotation = new ThreeFloats(
                (float) Math.toRadians(rotation.x()),
                (float) Math.toRadians(rotation.y()),
                (float) Math.toRadians(rotation.z())
        );
        if (rotation.x() == 0 && rotation.y() == 0 && rotation.z() == 0) {
            return;
        }
        Vector x_rotation = new Vector(new double[][]{
                {1, 0, 0},
                {0, Math.cos(rotation.x()), -Math.sin(rotation.x())},
                {0, Math.sin(rotation.x()), Math.cos(rotation.x())}}
        );
        Vector y_rotation = new Vector(new double[][]{
                {Math.cos(rotation.y()), 0, Math.sin(rotation.y())},
                {0, 1, 0},
                {-Math.sin(rotation.y()), 0, Math.cos(rotation.y())}}
        );
        Vector z_rotation = new Vector(new double[][]{
                {Math.cos(rotation.z()), -Math.sin(rotation.z()), 0},
                {Math.sin(rotation.z()), Math.cos(rotation.z()), 0},
                {0, 0, 1}}
        );

        Vector points_vector = this.generate_points_vector();

        Vector rotated_points_vector = x_rotation.dot_product(points_vector);
        rotated_points_vector = y_rotation.dot_product(rotated_points_vector);
        rotated_points_vector = z_rotation.dot_product(rotated_points_vector);

        this.convert_vector_to_points(points_vector, rotated_points_vector);
        this.hasRotated = true;
    }

    private void convert_vector_to_points(Vector original_points_vector, Vector new_points_vector) {
        this.prev_points = this.points;
        this.points = new int[this.arrayUpperSize][this.arrayUpperSize][this.arrayUpperSize];
        for (int i = 0; i < this.arrayUpperSize; i++) {
            for (int j = 0; j < this.arrayUpperSize; j++) {
                for (int k = 0; k < this.arrayUpperSize; k++) {
                    this.points[i][j][k] = -1;
                }
            }
        }

        for (int index = 0; index < original_points_vector.values.length; index++) {
            ThreeInts old_pos = new ThreeInts(
                    (int) Math.round(original_points_vector.values[index][0]) + this.midPoint,
                    (int) Math.round(original_points_vector.values[index][1]) + this.midPoint,
                    (int) Math.round(original_points_vector.values[index][2]) + this.midPoint
            );
            ThreeInts new_pos = new ThreeInts(
                    (int) Math.round(new_points_vector.values[index][0]) + this.midPoint,
                    (int) Math.round(new_points_vector.values[index][1]) + this.midPoint,
                    (int) Math.round(new_points_vector.values[index][2]) + this.midPoint
            );
            if (new_pos.x() < 0 || new_pos.y() < 0 || new_pos.z() < 0) {
                continue;
            }
            if (new_pos.x() >= this.arrayUpperSize || new_pos.y() >= this.arrayUpperSize || new_pos.z() >= this.arrayUpperSize) {
                continue;
            }
            this.points[new_pos.x()][new_pos.y()][new_pos.z()] = this.prev_points[old_pos.x()][old_pos.y()][old_pos.z()];
        }
    }

    private Vector generate_points_vector() {
        double[][] points_vector = new double[this.arrayUpperSize * this.arrayUpperSize * this.arrayUpperSize][3];
        int index = 0;
        for (int x = 0; x < this.arrayUpperSize; x++) {
            for (int y = 0; y < this.arrayUpperSize; y++) {
                for (int z = 0; z < this.arrayUpperSize; z++) {
                    points_vector[index] = new double[]{x - this.midPoint, y - this.midPoint, z - this.midPoint};
                    index++;
                }
            }
        }
        return new Vector(points_vector);
    }

    public int getArrayUpperSize() {
        return this.arrayUpperSize;
    }
}
