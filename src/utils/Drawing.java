package utils;

public class Drawing {
    public static int[] scale(double scale, double... values) {
        int[] scaledValues = new int[values.length];
        for (int i = 0; i < scaledValues.length; i++) {
            scaledValues[i] = (int) (values[i] * scale);
        }
        return scaledValues;
    }
}
