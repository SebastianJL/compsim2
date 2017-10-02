package week1;

import utils.IO;
import utils.Array;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BinaryTree {

    private Node root;
    private Particle[] particles;

    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree(3, 5);
        IO.print(tree);
    }

    BinaryTree(int dimensions, int nparticles) {
        Random randomGenerator = new Random();
        randomGenerator.setSeed(randomGenerator.nextInt());
        particles = new Particle[nparticles];
        for (int i=0; i<particles.length; i++) {
            particles[i] = new Particle(3, randomGenerator);
        }
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

    private void buildTree(int index, Node currentNode) {
        if (currentNode.end - currentNode.end <= 8) {
            return;
        }

        double pivot = (currentNode.posMax[index] - currentNode.posMin[index]) / 2;
        int i = partition(particles, 0, particles.length-1, pivot, index);

        // Instantiate left node
        double[] lPosMin = currentNode.posMin.clone();
        double[] lPosMax = currentNode.posMax.clone();
        lPosMax[index] = pivot;
        int lstart = currentNode.start;
        int lend = i-1;
        currentNode.lChild = new Node(lPosMin, lPosMax, lstart, lend, currentNode);

        // Instantiate right node
        double[] rPosMin = currentNode.posMin.clone();
        double[] rPosMax = currentNode.posMax.clone();
        rPosMin[index] = pivot;
        int rstart = i;
        int rend = currentNode.end;
        currentNode.rChild = new Node(rPosMin, rPosMax, rstart, rend, currentNode);

        // Recurse
        int nextIndex = ++index % dimensions();
        buildTree(nextIndex, currentNode.lChild);
        buildTree(nextIndex, currentNode.rChild);
    }

    private int dimensions() {
        return particles[0].dimensions();
    }

    private ArrayList<Node> linearise() {
        ArrayList<Node> linearTree = new ArrayList<>();
        root.linearise(linearTree, 0);
        return linearTree;
    }

    public String toString() {
        ArrayList<Node> linearTree = linearise();
        StringBuilder sb = new StringBuilder();
        int pow = 0;
        for (int i=0; i<linearTree.size(); i++) {
            if (Math.pow(2, pow)-1 == i) {
                sb.append('\n');
            }
            Node node = linearTree.get(i);
            sb.append(IO.toString(particles, node.start, node.end));
            sb.append(" | ");
            pow += 1;
        }
        return sb.toString();
    }

    class Node {
        private double[] posMin, posMax;
        private int start, end;
        private Node lChild, rChild;
        private Node parent;

        Node(double[] posMin, double[] posMax, int start, int end, Node parent) {
            assert posMax.length==posMin.length;
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

    }


}

