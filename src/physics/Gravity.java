package physics;

import giant_galaxy.Node;
import giant_galaxy.Particle;

public class Gravity {

    double epsilon = 0.002;


    void updateForce(Particle particle1, Particle particle2, double[] force){
        double[] r = particle1.vect(particle2.position()); // r2 - r1

        for (int i = 0; i < r.length; i++) {
            force[i] += r[i]*particle1.mass()*particle2.mass()/(particle1.dist(particle2.position(), 3) + epsilon);
        }
    }

    void updateForce(Particle particle1, Node node, double[] force){
        double[] r = particle1.vect(node.centerOfMass);
        double dist = particle1.dist(node.centerOfMass, 1);
        double dist3 = Math.pow(dist, 3);
        double dist5 = Math.pow(dist, 5);
        double dist7 = Math.pow(dist, 7);
        double rjk_Mjk = 0;
        double rk_Mlk;

        for (int j = 0; j < force.length; j++) {
            for (int k = 0; k < force.length; k++) {
                    rjk_Mjk += r[j] * r[k] * node.multMoment[j][k];
            }
        }

        for (int l = 0; l < force.length; l++){ // all dimensions
            rk_Mlk = 0;
            for (int k = 0; k < force.length; k++) { // first multipole index
                rk_Mlk += 2*r[k]*node.multMoment[k][l];
            }
            force[l] += r[l]*node.mass/dist3 - 3/dist5*(r[l]*node.trace + rk_Mlk) + 15/dist7*r[l]*rjk_Mjk;
        }
    }
}

