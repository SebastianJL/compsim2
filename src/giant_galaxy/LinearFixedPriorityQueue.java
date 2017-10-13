package giant_galaxy;


/**
 * Fixed width priority queue that keeps n elements with the highest priority. I.e. elements with lower priority are
 * kicked out.
 */
public class LinearFixedPriorityQueue implements IFixedPriorityQueue {
    int[] indices;
    double[] r2List;
    int r2Max; // Index of maximal r2 in r2List.

    LinearFixedPriorityQueue(int size) {
        indices = new int[size];
        r2List = new double[size];
        r2Max = 0;

        for (int i = 0; i < indices.length; i++) {
            indices[i] = -1;
            r2List[i] = Double.POSITIVE_INFINITY;
        }
    }

    @Override
    public void insert(double r2, int index) {
        if (r2 < r2List[r2Max]) {
            r2List[r2Max] = r2;
            indices[r2Max] = index;

            // Linear search for new r2Max;
            double max = 0;
            for (int i = 0; i < r2List.length; i++) {
                if (r2List[i] > max) {
                    max = r2List[i];
                    r2Max = i;
                }
            }
        }
    }

    @Override
    public double max() {
        return r2List[r2Max];
    }

    int[] particles() {
        return indices.clone();
    }
}
