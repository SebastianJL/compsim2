package week1;

import utils.IO;

import java.util.Random;

public class Particle {
    private double[] position;

    Particle(int dimension, Random randomGenerator) {
        this.position = new double[dimension];
        for (int i = 0; i< position.length; i++) {
            position[i] = randomGenerator.nextDouble();
        }
    }

    public Particle(double[] coords) {
        this.position = coords;
    }

    public int dimension() {
        return position.length;
    }

    public double[] position() {
        return position;
    }

    public double position(int i) {
        return position[i];
    }

    public String toString() {
        return IO.toString(position);
    }

}
