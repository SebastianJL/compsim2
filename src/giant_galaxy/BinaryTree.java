package giant_galaxy;

import utils.Array;
import utils.Drawing;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

class BinaryTree {

    private final Node root;
    private final Particle[] particles;
//    int swaps = 0;
//    int comparisons = 0;
//    int operations = 0;
//    int partitions = 0;

    BinaryTree(int dimensions, int nParticles, Random randomGenerator) {
        particles = new Particle[nParticles];
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle(dimensions, randomGenerator);
        }
        double[] posMin = new double[dimensions];
        double[] posMax = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            posMin[i] = 0;
            posMax[i] = 1;
        }
        root = new Node(posMin, posMax, 0, particles.length - 1, null);
        buildTree(0, root);
    }

    /**
     * Partitions the particle array.
     * @post particles[lo<=x<i] < pivot && particles[i<=x<=hi] >= pivot
     * @param particles Array of particles.
     * @param lo Lower index of partition.
     * @param hi Higher index of partition.
     * @param pivot Value to sort particles by. (All lower ones and all higher ones.)
     * @param dimension Dimension (0, 1, 2, ...) to partition by.
     * @return index i such that the after condition is fulfilled.
     */
    private int partition(Particle[] particles, int lo, int hi, double pivot, int dimension) {
//        partitions++;
        int i = lo;
        int j = hi;
        while (true) { // comparisons++;
            while (i <= hi && particles[i].position(dimension) < pivot) i++; // comparisons += 2; operations++;
            while (j >= lo && particles[j].position(dimension) > pivot) j--; // comparisons += 2; operations++;
            if (i >= j) { // comparisons++;
                return i;
            }
            Array.swap(particles, i, j); // swaps++;
        }
    }

    /**
     * Recursively builds the BinaryTree with Nodes based on particles.
     * @param dimension Indicates the dimension at which to partition at this level of the recursion.
     * @param currentNode Node on which the algorithm currently acts upon.
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    private void buildTree(int dimension, Node currentNode) {
        if (currentNode.end - currentNode.start < 8) {
            return;
        }

        double pivot = (currentNode.posMax[dimension] + currentNode.posMin[dimension]) / 2;
        int i = partition(particles, currentNode.start, currentNode.end, pivot, dimension);
        int nextDimension = (dimension + 1) % dimensions();

        // Set left node parameters
        double[] lPosMin = currentNode.posMin.clone();
        double[] lPosMax = currentNode.posMax.clone();
        lPosMax[dimension] = pivot;
        int lStart = currentNode.start;
        int lEnd = i - 1;

        // Set right node parameters
        double[] rPosMin = currentNode.posMin.clone();
        double[] rPosMax = currentNode.posMax.clone();
        rPosMin[dimension] = pivot;
        int rStart = i;
        int rEnd = currentNode.end;

        // Recurse
        if (lEnd - lStart >= 0) {
            currentNode.lChild = new Node(lPosMin, lPosMax, lStart, lEnd, currentNode);
            buildTree(nextDimension, currentNode.lChild);
        }
        if (rEnd - rStart >= 0) {
            currentNode.rChild = new Node(rPosMin, rPosMax, rStart, rEnd, currentNode);
            buildTree(nextDimension, currentNode.rChild);
        }
    }

    private int dimensions() {
        return particles[0].dimensions();
    }


    double[] posMin() {
        return root.posMin.clone();
    }
    double posMin(int index) {
        return root.posMin[index];
    }

    double[] posMax() {
        return root.posMax.clone();
    }
    double posMax(int index) {
        return root.posMax[index];
    }

    /**
     * Paint the tree.
     * @param g Graphic from JPanel in which to paint.
     * @param scale Scale for drawing.
     */
    void paint(Graphics g, double scale) {
        root.paint(g, scale);
        g.setColor(Color.BLUE);
    }

    @SuppressWarnings("SpellCheckingInspection")
    int ballwalk(double[] pos, double rMax) {
        return root.ballwalk(pos, Math.pow(rMax, 2));
    }

    double distCentGrav(double[] pos, Node node){
        double dist2 = 0;

        double[] centGrav  = centerOfGravity(Arrays.copyOfRange(particles, node.start, node.end));
        for(int i=0; i<dimensions(); i++){
            dist2 += Math.pow(pos[i]-centGrav[i],2);
        }
        return dist2;
    }

    double[] centerOfGravity(Particle[] particles){
        int dimensions = dimensions();
        double[] centGrav = new double[dimensions];
        for(int i=0; i<dimensions; i++){
            for(int j=0; j<particles.length; j++){
                centGrav[i] += particles[j].position(i);
            }
            centGrav[i] /= particles.length;
        }
        return centGrav;
    }

    double kNearestNeighbours(double[] pos, int k) {
        FixedPriorityQueue queue = new LinearFixedPriorityQueue(k);
        kNearestNeighbours(pos, k, root, queue, 0);
        return queue.max();
    }

    void kNearestNeighbours(double[] pos, int k, Node currentNode, FixedPriorityQueue queue, int mode) {
        /*
        mode: 0 for dist2 (Nodenorm), 1 for CenterOfGravityNorm
         */
        double difference = 0;
        if(mode==0) {
            difference = currentNode.lChild.dist2(pos) - currentNode.rChild.dist2(pos);
        }
        else{
            difference = distCentGrav(pos, currentNode.lChild) - distCentGrav(pos, currentNode.rChild);
        }

        if (currentNode.isLeaf()) {
            for (int i = currentNode.start; i < currentNode.end; i++) {
                queue.insert(particles[i].dist2(pos), i);
            }
        }

        else if (currentNode.hasLeft() && currentNode.hasRight()) {
            if (difference<0) {
                kNearestNeighbours(pos, k, currentNode.lChild, queue, mode);
                if(currentNode.rChild.dist2(pos) < queue.max()) {
                    kNearestNeighbours(pos, k, currentNode.rChild, queue, mode);
                }
            } else {
                kNearestNeighbours(pos, k, currentNode.rChild, queue, mode);
                if(currentNode.lChild.dist2(pos) < queue.max()) {
                    kNearestNeighbours(pos, k, currentNode.lChild, queue, mode);
                }
            }
        }


        else if (currentNode.hasLeft()) {
            kNearestNeighbours(pos, k, currentNode.lChild, queue, mode);
        }

        else {
            kNearestNeighbours(pos, k, currentNode.rChild, queue, mode);
        }


    }


    /**
     * Basic Container for the BinaryTree.
     */
    @SuppressWarnings("SpellCheckingInspection")
    class Node {
        /**
         *
         */
        double[] posMin, posMax, center;
        int start, end;
        Node lChild, rChild;
        Node parent;


        Node(double[] posMin, double[] posMax, int start, int end, Node parent) {
            assert posMax.length == posMin.length;
            this.posMin = posMin;
            this.posMax = posMax;
            this.start = start;
            this.end = end;
            this.parent = parent;
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

        double dist2(double[] pos){
            double dist2 = 0;
            for (int i = 0; i < pos.length; i++){
                if (pos[i] < posMin[i])
                    {dist2 += Math.pow(posMin[i] - pos[i], 2); }
                else if (pos[i] > posMax[i])
                    {dist2 += Math.pow(posMax[i] - pos[i], 2); }
                else
                    {dist2 += 0; }
            }
            return dist2;
        }

        int ballwalk(double[] pos, double r2max) {
            int cnt = 0;
            if (isLeaf()) {
                for (int j = start; j <= end; j++){
                    if (particles[j].dist2(pos) < r2max){
                        cnt++;
                    }
                }
            }
            else {
                if (lChild.dist2(pos) < r2max){
                    cnt += lChild.ballwalk(pos, r2max);
                }
                if (rChild.dist2(pos) < r2max){
                    cnt += rChild.ballwalk(pos, r2max);
                }
            }
            return cnt;
        }

        void paint(Graphics g, double scale) {
            if (isLeaf()) {
                double x = posMin[0] * scale;
                double y = posMin[1] * scale;
                double width = (posMax[0] - posMin[0]) * scale;
                double height = (posMax[1] - posMin[1]) * scale;
                Rectangle scaledValues = Drawing.transform(posMin[0], posMin[1], posMax[0] - posMin[0],
                        posMax[1] - posMin[1], scale);
                g.setColor(Color.BLACK);
                g.drawRect(scaledValues.x, scaledValues.y, scaledValues.width, scaledValues.height);
                Random rand = new Random();
                g.setColor(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
                for (int i = start; i <= end; i++) {
                    particles[i].paint(g, scale, 7);
                }
            } else {
                if (hasLeft()) {
                    lChild.paint(g, scale);
                }
                if (hasRight()) {
                    rChild.paint(g, scale);
                }
            }
        }
    }
}

