package giant_galaxy;

import utils.Drawing;
import utils.IO;

import java.awt.*;
import java.util.Random;

public class Particle {
    private double[] position;

    private final double mass;

    public int number;

    Particle(int dimensions, Random randomGenerator) {
        this.position = new double[dimensions];
        for (int i = 0; i < position.length; i++) {
            position[i] = randomGenerator.nextDouble();
        }
        mass = 1; // !!?!
    }

    public Particle(double[] coordinates) {
        this.position = coordinates;
        this.mass = 1; // !!?!
    }

    int dimensions() {
        return position.length;
    }

    public double[] position() {
        return position.clone();
    }

    public double position(int i) {
        return position[i];
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

    public double[] vect(Particle particle){
        double[] vect = new double[particle.dimensions()];
        for(int i=0; i<particle.position;i++) {
            vect[i] = particle.position[i] - this.position[i];
        }
        return vect;
    }
}
