package data_structures;

import dist.BoxDist2;
import dist.IMetric;
import distributionGenerators.IGenerator;
import utils.Array;

import java.awt.*;
import java.util.Arrays;
import javax.swing.tree.DefaultMutableTreeNode;

public class BinaryTree {

    public final Node root;
    public final Particle[] particles;
    private boolean isBuilt = false;
//    int swaps = 0;
//    int comparisons = 0;
//    int operations = 0;
//    int partitions = 0;

    public BinaryTree(int dimensions, int nParticles, IGenerator randomGenerator) {
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
        buildTree(root);
    }

    public boolean isBuilt() {
        return isBuilt;
    }

    /**
     * Partitions the particle array.
     * @post indices[lo<=x<i] < pivot && indices[i<=x<=hi] >= pivot
     * @param particles Array of indices.
     * @param lo Lower index of partition.
     * @param hi Higher index of partition.
     * @param pivot Value to sort indices by. (All lower ones and all higher ones.)
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

    public void buildTree() {
        buildTree(this.root);
    }

    public void buildTree(Node root) {
        root.lChild = null;
        root.rChild = null;
        isBuilt = false;
        buildTree(0, root);
        isBuilt = true;
    }

    /**
     * Recursively builds the BinaryTree with Nodes based on indices.
     * @param dimension Indicates the dimension at which to partition at this level of the recursion.
     * @param currentNode Node on which the algorithm currently acts upon.
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public void buildTree(int dimension, Node currentNode) {

        //Particle array slicing
//        Particle[] nodeParticles = Arrays.copyOfRange(particles, currentNode.start, currentNode.end);

        if (currentNode.end - currentNode.start < 8) {
            for (int j = currentNode.start; j <= currentNode.end; j++) {
                particles[j].index = j;
            }
            currentNode.mass = Node.mass(currentNode);
            currentNode.centerOfMass = Node.centerOfMass
                    (currentNode);
            currentNode.RMax = Node.RMaxFromCoM(currentNode.centerOfMass, currentNode);
            currentNode.multMoment = Node.multiPoleM(currentNode.centerOfMass, currentNode);
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

        // Going up again. Fill gravity stuff
        if(currentNode.hasLeft() && currentNode.hasRight()){
            currentNode.mass = currentNode.lChild.mass + currentNode.rChild.mass;
            currentNode.centerOfMass = Node.CombinedCenterOfMass(currentNode.lChild.mass, currentNode.rChild.mass,
                            currentNode.lChild.centerOfMass, currentNode.rChild.centerOfMass);
            currentNode.RMax = Node.CombinedRMaxFromCombinedCoM
                    (currentNode.lChild.centerOfMass, currentNode.rChild.centerOfMass,
                            currentNode.centerOfMass, currentNode.lChild.RMax, currentNode.rChild.RMax);
            currentNode.multMoment = Node.combMultiPoleM(currentNode.lChild.mass, currentNode.lChild.centerOfMass,currentNode.lChild.multMoment,
                    currentNode.rChild.mass, currentNode.rChild.centerOfMass, currentNode.rChild.multMoment,currentNode.centerOfMass);
            currentNode.trace = Node.trace(currentNode.multMoment);
        }
        else if(currentNode.hasRight()){
            currentNode.mass = currentNode.rChild.mass;
            currentNode.centerOfMass = currentNode.rChild.centerOfMass;
            currentNode.RMax = currentNode.rChild.RMax;
            currentNode.multMoment = currentNode.rChild.multMoment;
            currentNode.trace = Node.trace(currentNode.multMoment);

        }
        else{
            currentNode.mass = currentNode.lChild.mass;
            currentNode.centerOfMass = currentNode.lChild.centerOfMass;
            currentNode.RMax = currentNode.lChild.RMax;
            currentNode.multMoment = currentNode.lChild.multMoment;
            currentNode.trace = Node.trace(currentNode.multMoment);

        }
    }

    public int dimensions() {
        return particles[0].dimensions();
    }


    public double[] posMin() {
        return root.posMin.clone();
    }
    public double posMin(int index) {
        return root.posMin[index];
    }

    public double[] posMax() {
        return root.posMax.clone();
    }
    public double posMax(int index) {
        return root.posMax[index];
    }

    /**
     * Paint the tree.
     * @param g Graphic from JPanel in which to paint.
     * @param scale Scale for drawing.
     */
    public void paint(Graphics g, double scale) {
        root.paint(g, scale);
        g.setColor(Color.BLUE);
    }

    public void buildTreeImage(DefaultMutableTreeNode root){
        this.root.buildTreeImage(root);
    }
    @SuppressWarnings("SpellCheckingInspection")
    int ballwalk(double[] pos, double rMax) {
        BoxDist2 boxDist2 = BoxDist2.getInstance();
        return root.ballwalk(pos, Math.pow(rMax, 2), boxDist2);
    }

    /**
     * Calculates minimal radius squared including the k nearest neighbours.
     * @param pos Center from which to search the k nearest neighbours.
     * @param k Number of particles to search.
     * @param metric Metric to measure distance.
     * @return Minimal radius squared.
     */
    IFixedPriorityQueue kNearestNeighbours(double[] pos, int k, IMetric metric) {
        IFixedPriorityQueue queue = new LinearFixedPriorityQueue(k);
        kNearestNeighbours(pos, k, root, queue, metric);
        return queue;
    }

    void kNearestNeighbours(double[] pos, int k, Node currentNode, IFixedPriorityQueue queue, IMetric<Node> metric) {
        BoxDist2 dist2 = BoxDist2.getInstance();

        if (currentNode.isLeaf()) {
            for (int i = currentNode.start; i <= currentNode.end; i++) {
                queue.insert(particles[i].dist2(pos), i);
            }
        }
        else if (currentNode.hasLeft() && currentNode.hasRight()) {
            if (metric.metric(pos, currentNode.lChild) < metric.metric(pos, currentNode.rChild)) {
                kNearestNeighbours(pos, k, currentNode.lChild, queue, metric);
                if(dist2.metric(pos, currentNode.rChild) < queue.max()) {
                    kNearestNeighbours(pos, k, currentNode.rChild, queue, metric);
                }
            }
            else {
                kNearestNeighbours(pos, k, currentNode.rChild, queue, metric);
                if(dist2.metric(pos, currentNode.lChild) < queue.max()) {
                    kNearestNeighbours(pos, k, currentNode.lChild, queue, metric);
                }
            }
        }
        else if (currentNode.hasLeft() && dist2.metric(pos, currentNode.lChild) < queue.max()) {
            kNearestNeighbours(pos, k, currentNode.lChild, queue, metric);
        }
        else if (dist2.metric(pos, currentNode.rChild) < queue.max()) {
            kNearestNeighbours(pos, k, currentNode.rChild, queue, metric);
        }
    }
}

