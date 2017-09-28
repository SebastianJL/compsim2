package week1;

import utils.IO;
import utils.Array;

import java.util.Random;

public class BinaryTree {

    private Node root = new Node(3);

    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();
        Random randomGenerator = new Random();
        randomGenerator.setSeed(randomGenerator.nextInt());
        Particle[] particles = new Particle[5];
        for (int i=0; i<particles.length; i++) {
            particles[i] = new Particle(3, randomGenerator);
        }
        int i = tree.partition(particles, 0, particles.length-1,0.5, 0);
        IO.print("hello");
        IO.print(particles);
        IO.print(i);
    }

    private int partition(Particle[] particles, int lo, int hi, double pivot, int index) {
        int i = lo;
        int j = hi;
        while (true) {
            while (i <= hi && particles[i].position(index) < pivot) i++;
            while (j >= lo && particles[j].position(index) > pivot) j--;
            if (i >= j) {
                return i;
            }
            Array.swap(particles, i, j);
        }
    }

    class Node {
        private double posMin[], posMax[];
        private int start, end;
        private Node lChild, rChild;

        Node(int dimension) {
            this.posMax = new double[dimension-1];
            this.posMin = new double[dimension-1];
        }

        boolean isLeaf() {
            return start-end <= 8;
        }
    }
}

