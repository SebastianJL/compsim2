package giant_galaxy;

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

    public int dimensions() {
        return position.length;
    }

    public double[] position() {
        return position.clone();
    }

    public double position(int i) {
        return position[i];
    }

    public String toString() {
        return IO.toString(position);
    }

    public void paint(Graphics g, double scale, int size) {
        double x = position(0) * scale;
        double y = position(1) * scale;
        g.drawRect((int) x, (int) y, (int) size, (int) size);
    }

    double dist2(double[] pos){
        double dist2 = 0;
        for (int i = 0; i < dimensions(); i++){
            dist2 += Math.pow((pos[i] - position[i]) , 2);
        }
        return dist2;
    }
}
