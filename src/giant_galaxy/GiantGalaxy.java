package giant_galaxy;

import utils.Drawing;
import utils.IO;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GiantGalaxy extends JPanel {

    private static final long serialVersionUID = 1L;  //used for JPanel
    BinaryTree tree;
    double[] ballwalkCenter = new double[]{0.75, 0.25};
    double rMax = 0.1;

    public static void main(String[] argv) {
        GiantGalaxy galaxy = new GiantGalaxy();
        JFrame top = new JFrame("Galaxy");
        top.setBounds(00, 00, 900, 900);
        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        top.getContentPane().add(galaxy);
        top.setVisible(true);

        System.out.println("Start the engine");
        galaxy.run();
    }

    public void run() {
        Random randomGenerator = new Random();
        randomGenerator.setSeed(10);
        tree = new BinaryTree(2, 80, randomGenerator);
        repaint();
        int count = tree.ballwalk(ballwalkCenter, rMax);
        IO.print(count);

    }


    public void paint(Graphics g) {
        Rectangle bounds = getBounds();

        double scale = Drawing.scale(bounds, tree.posMin(), tree.posMax());

        // Clear window and draw background.
        g.setColor(Color.WHITE);
        paintComponent(g);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        // Draw tree.
        g.setColor(Color.BLACK);
        tree.paint(g, scale);

        // Draw circle for ballwalk.
        double[] widths = new double[]{2*rMax, 2*rMax};
        double[] centeredPosition = Drawing.center(ballwalkCenter, widths);
        int[] scaledPosition = Drawing.scaleValues(scale, centeredPosition);
        int[] scaledWidths = Drawing.scaleValues(scale, widths);
        g.drawOval(scaledPosition[0], scaledPosition[1], scaledWidths[1], scaledWidths[1]);


    }

}

 
 
 
 
 
 
 
 
	
	
