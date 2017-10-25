package physics;

import dist.BoxDist2;
import dist.IMetric;
import giant_galaxy.IFixedPriorityQueue;
import giant_galaxy.Node;
import giant_galaxy.Particle;
import utils.IO;

public class Interaction {

    // Precision factor
    public double theta = 0.12;

    double[] calcForce(Particle particle, Particle[] particles, Node currentNode, double[] force) {
        Gravity interActionObj = new Gravity();

        //behaviour if particle is in own leaf
        if (currentNode.isOwnLeaf(particle.number)) {
            for (int pNumber = currentNode.start; pNumber < currentNode.end; pNumber++) {
                double dist2 = particle.dist2(particles[pNumber].position());
                if (dist2 == 0) {
                    for (int i = 0; i < force.length; i++) {
                        force[i] += 0;
                    }
                } else {
                    double[] new_force = interActionObj.force(particle, particles[pNumber]);
                    for (int j = 0; j < force.length; j++) {
                        force[j] -= new_force[j];
                    }
                }
            }
            return force;
        //if not in own leaf
        } else if (currentNode.isLeaf()) {
            for (int i = currentNode.start; i < currentNode.end; i++) {
                double[] new_force = interActionObj.force(particle, particles[i]);
                for (int j = 0; j < force.length; j++) {
                    force[j] -= new_force[j];
                }
            }
            return force;

        } else if (theta > currentNode.RMax / particle.dist2(currentNode.centerOfGravity)) {
            // accept multipole
            double[] newForce = interActionObj.force(particle, currentNode);
            for (int i = 0; i<force.length;i++){
                force[i] -= newForce[i];
            }
            return force;
        }

        //else go deeper
        if (currentNode.hasLeft()) {
            return calcForce(particle, particles, currentNode.lChild, force);
        }

        if (currentNode.hasRight()) {
            return calcForce(particle, particles, currentNode.rChild, force);
        }

        //just to make IDE happy...
        else{
            IO.print("Something is very wrong.");
            return force;
        }
    }

}