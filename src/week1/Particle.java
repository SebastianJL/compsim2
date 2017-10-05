package week1;

import utils.IO;

import java.awt.*;
import java.util.Random;

public class Particle {
    private double[] position;

    Particle(int dimensions, Random randomGenerator) {
        this.position = new double[dimensions];
        for (int i = 0; i < position.length; i++) {
            position[i] = randomGenerator.nextDouble();
        }
    }

    public Particle(double[] coords) {
        this.position = coords;
    }

    public int dimensions() {
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

    public void paint(Graphics g, double scale, int size) {
        double x = position(0) * scale;
        double y = position(1) * scale;
        int width = size;
        int height = size;
        g.drawRect((int) x, (int) y, (int) width, (int) height);
    }

    double dist2(double[] pos){
        double dist2 = 0;
        for (int i = 0; i < dimensions(); i++){
            dist2 += Math.pow((pos[i] - position[i]) , 2);
        }
        return dist2;
    }
}
