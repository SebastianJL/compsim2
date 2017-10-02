package week1;

import utils.IO;
import utils.Array;

import java.awt.*;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BinaryTree {

    Node root;
    private Particle[] particles;


    BinaryTree(int dimensions, int nparticles) {
        Random randomGenerator = new Random();
        randomGenerator.setSeed(10);
        particles = new Particle[nparticles];
        for (int i=0; i<particles.length; i++) {
            particles[i] = new Particle(dimensions, randomGenerator);
        }
        double[] posMin = new double[dimensions];
        double[] posMax = new double[dimensions];
        for (int i=0; i<dimensions; i++) {
            posMin[i] = 0;
            posMax[i] = 1;
        }
        root = new Node(posMin, posMax, 0, particles.length-1, null, 0);
        buildTree(0, root);
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

    private void buildTree(int dimension, Node currentNode) {
        if (currentNode.end - currentNode.start < 8) {
            return;
        }

        double pivot = (currentNode.posMax[dimension] + currentNode.posMin[dimension]) / 2;
        int i = partition(particles, currentNode.start, currentNode.end, pivot, dimension);
        int nextDimension = (dimension+1) % dimensions();

        // Set left node parameters
        double[] lPosMin = currentNode.posMin.clone();
        double[] lPosMax = currentNode.posMax.clone();
        lPosMax[dimension] = pivot;
        int lstart = currentNode.start;
        int lend = i-1;

        // Set right node parameters
        double[] rPosMin = currentNode.posMin.clone();
        double[] rPosMax = currentNode.posMax.clone();
        rPosMin[dimension] = pivot;
        int rstart = i;
        int rend = currentNode.end;

        // Recurse
        if (lend - lstart >= 0) {
            currentNode.lChild = new Node(lPosMin, lPosMax, lstart, lend, currentNode, nextDimension);
            buildTree(nextDimension, currentNode.lChild);
        }
        if (rend - rstart >= 0) {
            currentNode.rChild = new Node(rPosMin, rPosMax, rstart, rend, currentNode, nextDimension);
            buildTree(nextDimension, currentNode.rChild);
        }

    }

    private int dimensions() {
        return particles[0].dimensions();
    }

    private ArrayList<Node> linearise() {
        int maxSize = 100000;
        ArrayList<Node> linearTree = new ArrayList<>(maxSize);
        for (int i=0; i<maxSize; i++) {
            linearTree.add(null);
        }
        root.linearise(linearTree, 0);
        return linearTree;
    }

    public String toString() {
        ArrayList<Node> linearTree = linearise();
        StringBuilder sb = new StringBuilder();
        int pow = 0;
        int dim = 0;
        for (int i=0; i<linearTree.size(); i++) {
            Node node = linearTree.get(i);
            if (Math.pow(2, pow)-1 == i) {
                sb.append('\n').append(dim).append(": ");
                pow += 1;
                dim = ++dim % dimensions();
            }
            if (node != null) {
                sb.append(node.start).append(", ").append(node.end);
            }
            else {
                sb.append("____");
            }
            sb.append(" | ");
        }
        return sb.toString();
    }

    void paint(Graphics g, Rectangle bounds) {
        double max = 0;
        for (int i=0; i<root.posMin.length; i++) {
            max = Math.max(root.posMax[i] - root.posMin[i], max);
        }
        double scale = Math.min(bounds.width, bounds.height) / max;
        root.paint(g, scale);
        g.setColor(Color.BLUE);
//        for (Particle particle : particles) {
//            particle.paint(g, scale, 3);
//        }
    }

    class Node {
        double[] posMin, posMax;
        int start, end;
        Node lChild, rChild;
        Node parent;
        int dimension;

        Node(double[] posMin, double[] posMax, int start, int end, Node parent, int dimension) {
            assert posMax.length==posMin.length;
            this.posMin = posMin;
            this.posMax = posMax;
            this.start = start;
            this.end = end;
            this.parent = parent;
            this.dimension = dimension;
        }

        boolean isLeftChild() {
            return this == parent.lChild;
        }

        boolean isLeaf() {
            return !(hasLeft() || hasRight());
        }

        boolean hasLeft() {
            return lChild != null;
        }

        boolean hasRight() {
            return rChild != null;
        }

        void linearise(ArrayList<Node> linearTree, int index) {
            linearTree.set(index, this);
            if (hasLeft()) {
                lChild.linearise(linearTree, index*2 + 1);
            }
            if (hasRight()) {
                rChild.linearise(linearTree, index*2 + 2);
            }
            return;
        }

        void paint(Graphics g, double scale) {
            if (!isLeaf()) {
                if (hasLeft()) {
                    lChild.paint(g, scale);
                }
                if (hasRight()) {
                    rChild.paint(g, scale);
                }
            }
            else {
                double x = posMin[0] * scale;
                double y = posMin[1] * scale;
                double width = (posMax[0] - posMin[0]) * scale;
                double height = (posMax[1] - posMin[1]) * scale;
                int randomColor0 = (int)(Math.random() * 255);
                int randomColor1 = (int)(Math.random() * 255);
                int randomColor2 = (int)(Math.random() * 255);
                g.setColor(Color.BLACK);
                g.drawRect((int)x, (int)y, (int)width, (int)height);
                g.setColor(new Color(randomColor0, randomColor1, randomColor2));
                for (int i=start; i<=end; i++) {
                    particles[i].paint(g, scale, 6);
                }
            }
        }

    }


}

