package main;

import distributionGenerators.IGenerator;
import distributionGenerators.NormalGenerator;
import data_structures.BinaryTree;
import data_structures.Particle;
import physics.Interaction;
import utils.Drawing;

import javax.swing.*;
import java.awt.*;

public class TimeEvolution extends JPanel {
    private static final long serialVersionUID = 1L;  //used for JPanel

    static BinaryTree tree;

    //initial conditions
    static final IGenerator RANDOM_GENERATOR = new NormalGenerator();
    static final int RANDOM_SEED = 42;
    static final int DIMENSIONS = 2;
    static final int N_PARTICLES = (int)1e3;

    //numerical constants
    static final double TIME_STEP = 0.00008d;
    static final double END_TIME = 100d;
    public static final double THETA = 0.12;
    static final int SLEEP = 20;

    public static void main(String[] args) {
        TimeEvolution timeEvolution = new TimeEvolution();
        RANDOM_GENERATOR.setSeed(RANDOM_SEED);
        tree = new BinaryTree(DIMENSIONS, N_PARTICLES, RANDOM_GENERATOR);

        timeEvolution.run();
    }

    public void run(){
        JFrame top = new JFrame("MovingGalaxy");
        top.setBounds(0, 0, 900, 900);
        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        top.getContentPane().add(this);
        top.setVisible(true);


        double time = 0;
        while(time< END_TIME){
            for (int i = 0; i < 5; i++) {
                leapFrog(TIME_STEP, tree);
                time += TIME_STEP;
            }

            repaint();
            try {
                //wait after every calculation to slow motion down
                Thread.sleep(SLEEP);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }


    public void leapFrog(double timeStep, BinaryTree tree) {
        Particle[] particles = tree.particles;

        //drift
        for (int i=0; i<particles.length; i++) {
            for (int dim=0; dim<particles[i].position().length; dim++) {
                particles[i].addToPosition(dim, 0.5*timeStep*particles[i].velocity(dim));
            }
        }

        //kick
        tree.buildTree();
        double[] force = new double[particles[0].position().length];
        for (int i=0; i<particles.length; i++) {
            // reset force
            for (int j = 0; j < force.length; j++) {
                force[j] = 0;
            }
            Interaction.calcForce(particles[i], particles, tree.root, force);
            for (int dim = 0; dim < particles[i].position().length; dim++) {
                particles[i].addToVelocity(dim, timeStep*force[dim]/particles[i].mass());
            }
        }

        //drift
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

        Toolkit.getDefaultToolkit().sync();
    }




}
