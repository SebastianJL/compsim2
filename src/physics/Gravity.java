package physics;

import giant_galaxy.Node;
import giant_galaxy.Particle;
import utils.IO;

public class Gravity {

    double epsilon = 0.002;


    void updateForce(Particle particle1, Particle particle2, double[] force){
        double[] r = particle1.vect(particle2.position()); // r2 - r1

        for (int i = 0; i < r.length; i++) {
            force[i] += r[i]*particle1.mass()*particle2.mass()/(particle1.dist(particle2.position(), 3) + epsilon);
        }
    }

    void updateForce(Particle particle1, Node node, double[] force){
        double[] r = particle1.vect(node.centerOfGravity);
        double dist = particle1.dist(node.centerOfGravity, 1);
        double dist3 = Math.pow(dist, 3);
        double dist5 = Math.pow(dist, 5);
        double dist7 = Math.pow(dist, 7);
        double rM;
        double rM2;

        for (int l = 0; l < force.length; l++){ // all dimensions
            for (int k=0; k < force.length; k++) { // first multipole index
                rM2 = 2*r[k]*node.multMoment[k][l];
                for (int j = 0; j < force.length; j++) { // second multipole index
                    rM = r[j] * r[k] * node.multMoment[j][k];
                    force[l] += r[l]*node.mass/dist3 - 3/dist5*(r[l]*node.trace + rM2) + 15/dist7*r[l]*rM;
                }
            }
        }
    }
}

