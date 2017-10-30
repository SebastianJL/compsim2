package giant_galaxy;

import distributionGenerators.IGenerator;
import utils.Drawing;
import utils.IO;

import java.awt.*;
import java.util.Random;

public class Particle {
    private double[] position;
    private double[] velocity;

    private final double mass;

    public int index; // describes particle index in particles array of binarytree when isBuilt==true

    Particle(int dimensions, IGenerator randomGenerator) {
        this.position = new double[dimensions];

        for (int i = 0; i < position.length; i++) {
            position[i] = randomGenerator.next();
        }
        mass = 1;
        this.velocity = new double[dimensions()];

    }

    public Particle(double[] coordinates) {
        this.position = coordinates;
        this.mass = 1;
        this.velocity = new double[dimensions()];
    }

    int dimensions() {
        return position.length;
    }

    public double[] position() {
        return position.clone();
    }

    public double position(int i) { return position[i]; }

    public void addToPosition(int i, double position) {
        this.position[i] += position;
    }

    public double[] velocity() { return velocity.clone(); }

    public double velocity(int i) { return velocity[i]; }

    public void addToVelocity(int i, double velocity) {
        this.velocity[i] += velocity;
    }

    public double mass() { return mass; }




    public String toString() {
        return IO.toString(position);
    }

    void paint(Graphics g, double scale, int size) {
        Rectangle scaledValues = Drawing.transform(position(0), position(1), size/scale, size/scale, scale, true);
        g.fillRect(scaledValues.x, scaledValues.y, scaledValues.width, scaledValues.height);
    }

    public double dist2(double[] pos){
        double dist2 = 0;
        for (int i = 0; i < dimensions(); i++){
            dist2 += Math.pow((pos[i] - position[i]) , 2);
        }
        return dist2;
    }

    /**
     * Distance vector from this to other.
     * @param other Vector describing position of other.
     * @return Distance vector.
     */
    public double[] vect(double[] other){
        double[] vect = new double[other.length];
        for(int i =0; i<other.length;i++) {
            vect[i] = other[i] - this.position[i];
        }
        return vect;
    }
}
