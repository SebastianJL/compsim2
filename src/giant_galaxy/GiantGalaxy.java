package giant_galaxy;

import utils.Drawing;
import utils.IO;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.Random;

@SuppressWarnings("SpellCheckingInspection")
class GiantGalaxy extends JPanel {

    private static final long serialVersionUID = 1L;  //used for JPanel
    private BinaryTree tree;
    private final double[] ballwalkCenter = new double[]{0.75, 0.25};
    private final double rMax = 0.1;

    public static void main(String[] argv) {
        GiantGalaxy galaxy = new GiantGalaxy();

        JFrame top = new JFrame("Galaxy");

        top.setBounds(0, 0, 900, 900);
        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        top.getContentPane().add(galaxy);


        System.out.println("Start the engine");
        galaxy.run();
        top.setVisible(true);
    }

    private void run() {
        Random randomGenerator = new Random();
        randomGenerator.setSeed(10);

        JFrame top2 = new JFrame("Tree structure");
        top2.setBounds(0, 0, 900, 900);
        top2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tree = new BinaryTree(2, 80, randomGenerator);
        showTree(tree,top2);

        repaint();

        int nParticlesInRMax = tree.ballwalk(ballwalkCenter, rMax);
        IO.print(nParticlesInRMax);

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
        Rectangle scaledValues = Drawing.transform(ballwalkCenter[0], ballwalkCenter[1], 2*rMax, 2*rMax,
                scale, true);
        g.drawOval(scaledValues.x, scaledValues.y, scaledValues.width, scaledValues.height);
    }

    public void showTree(BinaryTree tree,JFrame frame){

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

 
 
 
 
 
 
 
 
	
	
