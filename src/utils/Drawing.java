package utils;

import java.awt.*;

public class Drawing {

    public static double scale(Rectangle bounds, double[] minima, double[] maxima) {
        double max = 0;
        for (int i = 0; i < minima.length; i++) {
            max = Math.max(maxima[i] - minima[i], max);
        }
        return Math.min(bounds.width, bounds.height) / max;
    }

    public static int[] transform(double x, double y, double width, double height, double scale) {
        // Center values about x, y
        x -= width/2;
        y -= height/2;

        // Scale values
        int xScaled = (int) (x * scale);
        int yScaled = (int) (y * scale);
        int widthScaled = (int) (width * scale);
        int heightScaled = (int) (height * scale);

        return new int[]{xScaled, yScaled, widthScaled, heightScaled};
    }
}
