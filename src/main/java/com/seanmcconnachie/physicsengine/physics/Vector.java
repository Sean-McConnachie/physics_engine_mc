package com.seanmcconnachie.physicsengine.physics;

public class Vector implements java.io.Serializable {
    public double[][] values;

    public Vector(double[][] values) {
        this.values = values;
    }

    public Vector dot_product(Vector other) {
        double[][] result = new double[other.values.length][other.values[0].length];
        for (int new_col = 0; new_col < other.values.length; new_col++) {
            for (int new_row = 0; new_row < other.values[0].length; new_row++) {
                double sum = 0;
                for (int this_col = 0; this_col < this.values.length; this_col++) {
                    sum += this.values[this_col][new_row] * other.values[new_col][this_col];
                }
                result[new_col][new_row] = sum;
            }
        }
        return new Vector(result);
    }

    public double magnitude() {
        double sum = 0;
        for (double[] value : this.values) {
            for (int row = 0; row < this.values[0].length; row++) {
                sum += Math.pow(value[row], 2);
            }
        }
        return Math.sqrt(sum);
    }

    public Vector normalize() {
        double[][] result = new double[this.values.length][this.values[0].length];
        double magnitude = this.magnitude();
        for (int col = 0; col < this.values.length; col++) {
            for (int row = 0; row < this.values[0].length; row++) {
                result[col][row] = this.values[col][row] / magnitude;
            }
        }
        return new Vector(result);
    }
}
