package week1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberTest {
    @Test
    void getValue() {
        Number n = new Number(3);
        assertEquals(3, n.getValue());
    }

    @Test
    void add() {
        Number one = new Number(1);
        Number two = new Number(2);
        assertEquals(3, one.add(two).getValue());
    }

}