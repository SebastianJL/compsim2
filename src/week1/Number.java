package week1;

class Number {

    private final double value;

    Number(double value) {
        this.value = value;
    }

    double getValue() {
        return value;
    }

    Number add(Number other) {
        return new Number(this.getValue() + other.getValue());
    }
}
