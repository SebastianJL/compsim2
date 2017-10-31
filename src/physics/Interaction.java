package physics;

import giant_galaxy.Node;
import giant_galaxy.Particle;
import utils.IO;

public class Interaction {

    // Precision factor
    public double theta = 0.12;

    /**
     * Calculate updateForce on one particle from all others contained in currentNode.
     * @param particle Particle to calculate updateForce for.
     * @param particles All particles in the system.
     * @param currentNode Node under inspection.
     * @param force Vector for updateForce summation.
     */
    void calcForce(Particle particle, Particle[] particles, Node currentNode, double[] force) {
        Gravity interActionObj = new Gravity();

        //behaviour if particle is in own leaf
        if (currentNode.isLeaf()) {
            if (currentNode.contains(particle.index)) {
                for (int i = currentNode.start; i < particle.index; i++) {
                    interActionObj.updateForce(particle, particles[i], force);
                }
                for (int i = particle.index + 1; i <= currentNode.end; i++) {
                    interActionObj.updateForce(particle, particles[i], force);
                }
            } else {
                for (int i = currentNode.start; i <= currentNode.end; i++) {
                    interActionObj.updateForce(particle, particles[i], force);
                }
            }
        }

        // no leaf, far away, thus use multipole
        else if (theta > currentNode.RMax / particle.dist2(currentNode.centerOfGravity)) {
            // accept multipole
            interActionObj.updateForce(particle, currentNode, force);
        }

        // no leaf, recurse
        else {
            if (currentNode.hasLeft()) {
                calcForce(particle, particles, currentNode.lChild, force);
            }

            if (currentNode.hasRight()) {
                calcForce(particle, particles, currentNode.rChild, force);
            }
        }
    }
}