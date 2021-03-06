package main;

import distributionGenerators.IGenerator;
import distributionGenerators.NormalGenerator;
import data_structures.BinaryTree;
import data_structures.IFixedPriorityQueue;
import physics.Interaction;
import utils.Drawing;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

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

//        JFrame top = new JFrame("Galaxy");
//
//        top.setBounds(0, 0, 900, 900);
//        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        top.getContentPane().add(galaxy);

        System.out.println("Start the engine");
        galaxy.run();
//        top.setVisible(true);
    }

    private void run() {
        IGenerator randomGenerator1 = new NormalGenerator();
        randomGenerator1.setSeed(10);
        IGenerator randomGenerator2 = new NormalGenerator();
        randomGenerator2.setSeed(12);

        Interaction interaction = new Interaction();
        tree = new BinaryTree(2, (int) 1e2, randomGenerator1, randomGenerator2);


//        int nParticlesInRMax = tree.ballwalk(ballwalkCenter, rMax);

//        IMetric cogDist2 = CenterOfGravityDist2.getInstance();
//        queue = tree.kNearestNeighbours(ballwalkCenter, nParticlesInRMax, cogDist2);
//
//        IMetric boxDist2 = BoxDist2.getInstance();
//        queue = tree.kNearestNeighbours(ballwalkCenter, nParticlesInRMax, boxDist2);
//
//        rKNN = Math.sqrt(queue.max());
//        IO.print("nParticlesInRMax: " + nParticlesInRMax);
//        IO.print("rMax: " + rMax);
//        IO.print("rKNN: " + rKNN);
//        repaint();
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

//            // Draw circle for kNN
//            Rectangle kNNCoords = Drawing.transform(ballwalkCenter[0], ballwalkCenter[1], 2 * rKNN, 2 * rKNN,
//                    scale, true);
//            g.setColor(Color.RED);
//            g.drawOval(kNNCoords.x, kNNCoords.y, kNNCoords.width, kNNCoords.height);
//
//            // Draw particles in rKNN
//            for (int i : queue.indices()) {
//                tree.particles[i].paint(g, scale, 8);
//            }

        }
    }

    public void showTree(BinaryTree tree, JFrame frame){

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        JTree jt = new JTree(root);
        frame.getContentPane().add(jt);
        frame.setVisible(true);

        tree.buildTreeImage(root);

        for (int i = 0; i < jt.getRowCount(); i++)
        {
            jt.expandRow(i);
        }
    }
}

 
 
 
 
 
 
 
 
	
	
