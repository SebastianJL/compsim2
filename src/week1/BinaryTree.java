package week1;

import utils.IO;

public class BinaryTree {


    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();
        double[][] a = new double[][]{{5, 5}, {1, 4}, {4, 3}, {3, 2}, {4, 1}};
        tree.quicksort(a, 0, a.length-1);
        IO.print(a);
        IO.print("hello");
    }

    private int partition(double[][] data, int lo, int hi, double pivot, int dimension) {
        int i = lo - 1;
        int j = hi + 1;
        int index = dimension-1;
        while (true) {
            while (data[++i][index] < pivot && i < j);
            while (data[--j][index] > pivot && j > i);
            if (i >= j) {
                return j;
            }
            swap(data, i, j, index);
        }
    }

    private void swap(double[][] a, int i, int j, int index) {
        double[] tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }


}

