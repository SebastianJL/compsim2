package week1;

/**
 * This class wraps a number. Not very sencible though^^
 */
class Number {

    private final double value;

    Number(double value) {
        this.value = value;
    }

    /**
     * @return Value of the number.
     */
    double getValue() {
        return value;
    }

    /** Adds two numbers.
     * @param other Other number to add
     * @return New number with added values.
     */
    Number add(Number other) {
        return new Number(this.getValue() + other.getValue());
    }
}
