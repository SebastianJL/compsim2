package giant_galaxy;

import dist.BoxDist2;
import dist.IMetric;
import utils.Array;

import java.awt.*;
import java.util.Random;

public class BinaryTree {

    private final Node root;
    public final Particle[] particles;
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
        root = new Node(posMin, posMax, 0, particles.length - 1, null, this);
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
            currentNode.lChild = new Node(lPosMin, lPosMax, lStart, lEnd, currentNode, this);
            buildTree(nextDimension, currentNode.lChild);
        }
        if (rEnd - rStart >= 0) {
            currentNode.rChild = new Node(rPosMin, rPosMax, rStart, rEnd, currentNode, this);
            buildTree(nextDimension, currentNode.rChild);
        }
    }

    public int dimensions() {
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
        BoxDist2 boxDist2 = BoxDist2.getInstance();
        return root.ballwalk(pos, Math.pow(rMax, 2), boxDist2);
    }

    double kNearestNeighbours(double[] pos, int k, IMetric metric) {
        IFixedPriorityQueue queue = new LinearFixedPriorityQueue(k);
        kNearestNeighbours(pos, k, root, queue, metric);
        return queue.max();
    }

    void kNearestNeighbours(double[] pos, int k, Node currentNode, IFixedPriorityQueue queue, IMetric<Node> metric) {

        if (currentNode.isLeaf()) {
            for (int i = currentNode.start; i <= currentNode.end; i++) {
                queue.insert(particles[i].dist2(pos), i);
            }
        }

        else if (currentNode.hasLeft() && currentNode.hasRight()) {

            double difference = metric.metric(pos,currentNode.lChild)-metric.metric(pos,currentNode.rChild);

            if (difference<0) {
                if((queue.max() == Double.POSITIVE_INFINITY) || (currentNode.lChild.dist2(pos) < queue.max())) {
                    kNearestNeighbours(pos, k, currentNode.lChild, queue, metric);
                }
                if((queue.max() == Double.POSITIVE_INFINITY) || (currentNode.rChild.dist2(pos) < queue.max())) {
                    kNearestNeighbours(pos, k, currentNode.rChild, queue, metric);
                }
            }
            else {
                if((queue.max() == Double.POSITIVE_INFINITY) || (currentNode.rChild.dist2(pos) < queue.max())) {
                    kNearestNeighbours(pos, k, currentNode.rChild, queue, metric);
                }
                if((queue.max() == Double.POSITIVE_INFINITY) || (currentNode.lChild.dist2(pos) < queue.max())) {
                    kNearestNeighbours(pos, k, currentNode.lChild, queue, metric);
                }
            }
        }


        else if (currentNode.hasLeft()) {
            kNearestNeighbours(pos, k, currentNode.lChild, queue, metric);
        }

        else {
            kNearestNeighbours(pos, k, currentNode.rChild, queue, metric);
        }


    }


}

