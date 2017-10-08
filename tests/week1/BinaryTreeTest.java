package week1;

import utils.IO;

import java.util.Random;

public class BinaryTreeTest {

    public static void main(String[] args) {
        Random randomGenerator = new Random();
        randomGenerator.setSeed(10);
        int nSimulations = 1000;
        double[] nParticles = new double[]{1e1, 1e2, 1e3, 1e4, 1e5, 1e6};
        int modulo = nParticles.length;
        double[] swaps = new double[modulo];
        double[] comparisons = new double[modulo];
        double[] partitions = new double[modulo];

        for (int i = 0; i < nSimulations; i++) {
            BinaryTree tree = new BinaryTree(2, (int) nParticles[i%modulo], randomGenerator);
            swaps[i%modulo] += tree.swaps;
            comparisons[i%modulo] += tree.comparisons;
            partitions[i%modulo] += tree.partitions;
        }

        for (int i = 0; i < modulo; i++) {
            swaps[i%modulo] /= nSimulations / modulo * nParticles[i];
            comparisons[i%modulo] /= nSimulations / modulo * nParticles[i];
            partitions[i%modulo] /= nSimulations / modulo * nParticles[i];
        }
        

        IO.print("nSimulations:");
        IO.print(nSimulations);
        IO.print("\nnParticles:");
        IO.print(nParticles);
        IO.print("\nSwaps:");
        IO.print(swaps);
        IO.print("\nComparisons:");
        IO.print(comparisons);
        IO.print("\nPartitions:");
        IO.print(partitions);


    }
}
