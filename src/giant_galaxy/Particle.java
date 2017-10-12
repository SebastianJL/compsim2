package giant_galaxy;

import utils.Drawing;
import utils.IO;

import java.awt.*;
import java.util.Random;

class Particle {
    private double[] position;

    Particle(int dimensions, Random randomGenerator) {
        this.position = new double[dimensions];
        for (int i = 0; i < position.length; i++) {
            position[i] = randomGenerator.nextDouble();
        }
    }

    public Particle(double[] coordinates) {
        this.position = coordinates;
    }

    int dimensions() {
        return position.length;
    }

    public double[] position() {
        return position.clone();
    }

    double position(int i) {
        return position[i];
    }

    public String toString() {
        return IO.toString(position);
    }

    void paint(Graphics g, double scale, int size) {
        Rectangle scaledValues = Drawing.transform(position(0), position(1), size/scale, size/scale, scale, true);
        g.fillRect(scaledValues.x, scaledValues.y, scaledValues.width, scaledValues.height);
    }

    double dist2(double[] pos){
        double dist2 = 0;
        for (int i = 0; i < dimensions(); i++){
            dist2 += Math.pow((pos[i] - position[i]) , 2);
        }
        return dist2;
    }
}
