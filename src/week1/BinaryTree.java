package week1;


public class BinaryTree {


    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();
        double[] a = new double[]{5, 1, 4, 3, 4};
        tree.quicksort(a, 0, a.length-1, 1);
        tree.printArray(a);
    }

    public void quicksort(double[] data, int lo, int hi, double dim) {
        if (lo < hi) {
            double piv = data[lo];
            int part = partition(data, lo, hi, piv, dim);
            quicksort(data, lo, part, dim);
            quicksort(data, part+1, hi, dim);
        }
    }

    public int partition(double[] data, int lo, int hi, double pivot, double dimension) {
        int i = lo - 1;
        int j = hi + 1;
        while (true) {
            while (data[++i] < pivot);
            while (data[--j] > pivot);
            if (i >= j) {
                return j;
            }
            swap(data, i, j);
        }
    }


    private void swap(double[] a, int i, int j) {
        double tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    private void printArray(double[] a) {
        System.out.print("[");
        for (int i=0; i<a.length; i++) {
            System.out.print(a[i] + ",");
        }
        System.out.println("]");
    }
}

