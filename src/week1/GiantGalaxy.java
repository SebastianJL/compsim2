package week1;

import utils.IO;

import javax.swing.*;
import java.awt.*;

public class GiantGalaxy extends JPanel {

    private static final long serialVersionUID = 1L;  //used for JPanel
    BinaryTree tree;

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
        tree = new BinaryTree(2, 80);
//        IO.print(tree.root.start + ", " + tree.root.end);
//        IO.print(tree.root.lChild.start + ", " + tree.root.lChild.end);
//        IO.print(tree.root.rChild.start + ", " + tree.root.rChild.end);
//        IO.print(tree);
        repaint();
        int count = tree.ballwalk(new double[]{0.5,0.5}, 0.25);

        IO.print(count);

    }


    public void paint(Graphics g) {
        Rectangle bounds = getBounds();

        // Calculate scale
        double max = 0;
        for (int i = 0; i < tree.posMin().length; i++) {
            max = Math.max(tree.posMax(i) - tree.posMin(i), max);
        }
        double scale = Math.min(bounds.width, bounds.height) / max;

        // Clear window and draw background
        g.setColor(Color.WHITE);
        paintComponent(g);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
//        g.drawLine((int) x3D[i] ,(int) y3D[i],  x3D[i+1] ,(int) y3D[i+1]);
//        g.fillRect();
        g.setColor(Color.BLACK);
        tree.paint(g, scale);
//        g.drawOval(0.25,0.25, 0.5, 0.5);


    }

}

 
 
 
 
 
 
 
 
	
	
