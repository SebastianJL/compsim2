package giant_galaxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.IO;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LinearFixedPriorityQueueTest {

    @Test
    public void testConstructor() {
        int size = 8;
        LinearFixedPriorityQueue queue = new LinearFixedPriorityQueue(size);

        assertEquals(Double.POSITIVE_INFINITY, queue.max());
    }

    @Test
    public void testFunctionality() {
        int size = 3;
        LinearFixedPriorityQueue queue = new LinearFixedPriorityQueue(size);
        int i;

        // Fill up queue with radii2 of same length.
        for (i = 0; i < size; i++) {
             queue.insert(1, i);
        }
        assertEquals(queue.max(), 1);

        // Insert radius2 that's longer than max.
        int [] before = queue.indices();
        queue.insert(2, ++i);
        assertEquals(queue.max(), 1);
        assertArrayEquals(before, queue.indices());

        // Insert radius2 that's smaller than max.
        queue.insert(0.5, ++i);
        assertEquals(queue.max(), 1);
        assertFalse(Arrays.equals(before, queue.indices()));

        // Max gets smaller when all with length 1 are eliminated.
        queue.insert(0.5, ++i);
        queue.insert(0.5, ++i);
        assertEquals(queue.max(), 0.5);
    }
}