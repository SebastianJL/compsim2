package week1;

import utils.IO;

import java.util.Random;

public class BinaryTreeTest {

    public static void main(String[] args) {
        Random randomGenerator = new Random();
        randomGenerator.setSeed(10);
        int nSimulations = 1000;
        double[] nParticles = new double[]{
                1e1, 2e1, 3e1, 4e1, 5e1, 6e1, 7e1, 8e1, 9e1,
                1e3, 2e2, 3e2, 4e2, 5e2, 6e2, 7e2, 8e2, 9e2,
                1e4, 2e4, 3e4, 4e4, 5e4, 6e4, 7e4, 8e4, 9e4,
                1e5, 2e5, 3e5, 4e5, 5e5, 6e5, 7e5, 8e5, 9e5,
                1e6};
        int modulo = nParticles.length;
        double[] swaps = new double[modulo];
        double[] comparisons = new double[modulo];
        double[] partitions = new double[modulo];
        double[] operations = new double[modulo];

        for (int i = 0; i < nSimulations; i++) {
            BinaryTree tree = new BinaryTree(2, (int) nParticles[i%modulo], randomGenerator);
            swaps[i%modulo] += tree.swaps;
            comparisons[i%modulo] += tree.comparisons;
            partitions[i%modulo] += tree.partitions;
            operations[i%modulo] += tree.operations;
        }

        for (int i = 0; i < modulo; i++) {
            swaps[i%modulo] /= nSimulations / modulo * nParticles[i];
            comparisons[i%modulo] /= nSimulations / modulo * nParticles[i];
            partitions[i%modulo] /= nSimulations / modulo * nParticles[i];
            operations[i%modulo] /= nSimulations / modulo * nParticles[i];
        }
        
        IO.print("nSimulations:");
        IO.print(nSimulations);
        IO.print("\nnParticles:");
        IO.print(nParticles);
        IO.print("\nSwaps:");
        IO.print(swaps);
        IO.print("\nComparisons:");
        IO.print(comparisons);
        IO.print("\nOperations:");
        IO.print(operations);
        IO.print("\nPartitions:");
        IO.print(partitions);
    }
}
