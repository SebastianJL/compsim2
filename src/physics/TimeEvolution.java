package physics;

import giant_galaxy.BinaryTree;
import giant_galaxy.Particle;
import utils.Drawing;

import javax.swing.*;
import java.awt.*;

public class TimeEvolution extends JPanel {
    private static final long serialVersionUID = 1L;  //used for JPanel


    Interaction interaction;
    BinaryTree tree;

    public TimeEvolution(Interaction interaction, BinaryTree tree){

        this.interaction = interaction;
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
            for (int i = 0; i < 5; i++) {
                leapFrog(timeStep, tree);
                time += timeStep;
            }

            repaint();
            try {
                //wait after every calculation to slow motion down
                Thread.sleep(2);
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
            interaction.calcForce(particles[i], particles, tree.root, force);
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
    }




}
