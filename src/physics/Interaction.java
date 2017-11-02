package physics;

import data_structures.Node;
import data_structures.Particle;
import main.TimeEvolution;

public class Interaction {
    /**
     * Calculate updateForce on one particle from all others contained in currentNode.
     * @param particle Particle to calculate updateForce for.
     * @param particles All particles in the system.
     * @param currentNode Node under inspection.
     * @param force Vector for updateForce summation.
     */
    public static void calcForce(Particle particle, Particle[] particles, Node currentNode, double[] force) {
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
        else if (TimeEvolution.THETA > currentNode.RMax / particle.dist2(currentNode.centerOfMass)) {
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