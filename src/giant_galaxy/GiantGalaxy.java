package giant_galaxy;

import dist.BoxDist2;
import dist.CenterOfGravityDist2;
import dist.IMetric;
import utils.Drawing;
import utils.IO;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

@SuppressWarnings("SpellCheckingInspection")
class GiantGalaxy extends JPanel {

    private static final long serialVersionUID = 1L;  //used for JPanel
    private BinaryTree tree;
    private final double[] ballwalkCenter = new double[]{0.5, 0.5};
    private final double rMax = 0.1;
    private double rKNN;
    IFixedPriorityQueue queue;

    public static void main(String[] argv) {
        GiantGalaxy galaxy = new GiantGalaxy();
        JFrame top = new JFrame("Galaxy");
        top.setBounds(0, 0, 900, 900);
        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        top.getContentPane().add(galaxy);
        top.setVisible(true);

        System.out.println("Start the engine");
        galaxy.run();
    }

    private void run() {
        Random randomGenerator = new Random();
        randomGenerator.setSeed(10);
        tree = new BinaryTree(2, (int) 1e2, randomGenerator);
        int nParticlesInRMax = tree.ballwalk(ballwalkCenter, rMax);

//        IMetric cogDist2 = CenterOfGravityDist2.getInstance();
//        queue = tree.kNearestNeighbours(ballwalkCenter, nParticlesInRMax, cogDist2);
//
        IMetric boxDist2 = BoxDist2.getInstance();
        queue = tree.kNearestNeighbours(ballwalkCenter, nParticlesInRMax, boxDist2);

        rKNN = Math.sqrt(queue.max());
        IO.print("nParticlesInRMax: " + nParticlesInRMax);
        IO.print("rMax: " + rMax);
        IO.print("rKNN: " + rKNN);
        repaint();
    }


    public void paint(Graphics g) {
        Rectangle bounds = getBounds();

        if (tree!= null && tree.isBuilt()) {
            double scale = Drawing.scale(bounds, tree.posMin(), tree.posMax());

            // Clear window and draw background.
            g.setColor(Color.WHITE);
            paintComponent(g);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

            // Draw tree.
            g.setColor(Color.BLACK);
            tree.paint(g, scale);

            // Draw circle for ballwalk.
            Rectangle ballwalkCoords = Drawing.transform(ballwalkCenter[0], ballwalkCenter[1], 2 * rMax, 2 * rMax,
                    scale, true);
            g.drawOval(ballwalkCoords.x, ballwalkCoords.y, ballwalkCoords.width, ballwalkCoords.height);

            // Draw circle for kNN
            Rectangle kNNCoords = Drawing.transform(ballwalkCenter[0], ballwalkCenter[1], 2 * rKNN, 2 * rKNN,
                    scale, true);
            g.setColor(Color.RED);
            g.drawOval(kNNCoords.x, kNNCoords.y, kNNCoords.width, kNNCoords.height);

            // Draw particles in rKNN
            for (int i : queue.indices()) {
                tree.particles[i].paint(g, scale, 8);
            }
        }
    }
}

 
 
 
 
 
 
 
 
	
	
