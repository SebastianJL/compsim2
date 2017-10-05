package utils;

import java.awt.*;

public class Drawing {
    /**
     * Scale values for a given scaleValues.
     * @param scale Scaling factor.
     * @param values Values to scaleValues.
     * @return Scaled values.
     */
    public static int[] scaleValues(double scale, double[] values) {
        int[] scaledValues = new int[values.length];
        for (int i = 0; i < scaledValues.length; i++) {
            scaledValues[i] = (int) (values[i] * scale);
        }
        return scaledValues;
    }

    public static double scale(Rectangle bounds, double[] minima, double[] maxima) {
        double max = 0;
        for (int i = 0; i < minima.length; i++) {
            max = Math.max(maxima[i] - minima[i], max);
        }
        return Math.min(bounds.width, bounds.height) / max;
    }


    public static double[] center(double[] pos, double[] widths) {
        double[] centeredPosition = new double[pos.length];
        for (int i = 0; i < centeredPosition.length; i++) {
            centeredPosition[i] = pos[i] - (widths[i]/2);
        }
        return centeredPosition;
    }
}
