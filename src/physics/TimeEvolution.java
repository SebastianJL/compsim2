package physics;

import giant_galaxy.BinaryTree;
import giant_galaxy.Particle;
import utils.Drawing;

import javax.swing.*;
import java.awt.*;

public class TimeEvolution extends JPanel {
    private static final long serialVersionUID = 1L;  //used for JPanel


    Interaction interaction;
    Particle[] particles;
    BinaryTree tree;

    public TimeEvolution(Interaction interaction, BinaryTree tree){

        this.interaction = interaction;
        this.particles = tree.particles;
        this.tree = tree;


    }

    public void run(double timeStep, double endTime){
        JFrame top = new JFrame("MovingGalaxy");
        top.setBounds(0, 0, 900, 900);
        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        top.getContentPane().add(this);
        top.setVisible(true);



        double time = 0;
        while(time<endTime){
            leapFrog(timeStep, particles, tree);
            tree.buildTree(tree.root);
            time += timeStep;

            try {
                //wait after every calculation to slow motion down
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            repaint();
        }

    }


    public void leapFrog(double timeStep, Particle[] particles, BinaryTree tree) {

//        double[][][] result = new double[2][bodies.length][3];


        //kick
        for (int i=0; i<particles.length; i++) {
            for (int dim=0; dim<particles[i].position().length; dim++) {
                particles[i].addToPosition(dim, 0.5*timeStep*particles[i].velocity(dim));
            }
        }

        //drift
        double[] nullForce = new double[particles[0].position().length];
        for (int i=0; i<particles.length; i++) {
            double[] force = interaction.calcForce(particles[i], particles, tree.root, nullForce );

            for (int dim=0; dim<particles[i].position().length; dim++) {
                particles[i].addToVelocity(dim, timeStep*force[dim]/particles[i].mass());
            }
        }

        //kick
        for (int i=0; i<particles.length; i++) {
            for (int dim=0; dim<particles[i].position().length; dim++) {
                particles[i].addToPosition(dim, 0.5*timeStep*particles[i].velocity(dim));
            }
        }

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

        }
    }




}
