package physics;

import giant_galaxy.Node;
import giant_galaxy.Particle;
import utils.IO;

public class Gravity {

    double[] force(Particle particle1, Particle particle2){
        double[] force = particle1.vect(particle2.position());
        for (int i = 0; i < force.length; i++) {
            force[i] *= particle1.mass() * particle2.mass() / particle1.dist2(particle2.position());
        }
        return force;
    }

    double[] force(Particle particle1, Node node){
        double[] force = new double[particle1.position().length];
        double[] r = particle1.vect(node.centerOfGravity);
        double dist = Math.sqrt(particle1.dist2(node.centerOfGravity));
        double rM = 0;
        double rM2 = 0;


        for (int l =0; l<force.length; l++){
            for (int k=0; k<force.length; k++) {
                rM2 = 2*r[k]*node.multMoment[k][l];
                for (int j = k; j < force.length; j++) {
                    rM = r[j] * r[k] * node.multMoment[j][k];
                    force[l] = node.mass/Math.pow(dist, 3) - 3/Math.pow(dist, 5)*(r[l]*node.trace + rM2) + 15/Math.pow(dist,7)*r[l]*rM;
                }
            }
        }

        return force;
    }

    double greenFkt(double r, int m){
        /**
         * calculate multipoles by recursion
         */
        if(r==0){
            IO.print("diving by zero!");
            return 0;
        }
        if(m<0){
            IO.print("m smaller than 0!");
            return 0;
        }

        if(m==0){
            return -1./r;
        }
        else{
            m -= 1;
            return -(2*m+1)/Math.pow(r,2)*greenFkt(r,m);
        }
    }


//    double[] ownLeafForce(Particle particle1, Particle particle2){
//        double[] force = particle1.vect(particle2);
//        double dist2 = particle1.dist2(particle2.position());
//        if (dist2 == 0) {
//            for (int i = 0; i < force.length; i++) {
//                force[i] = 0;
//            }
//            return force;
//        }
//        else {
//            for (int i = 0; i < force.length; i++) {
//                force[i] *= particle1.mass() * particle2.mass() / particle1.dist2(particle2.position());
//            }
//            return force;
//        }
//    }
}

