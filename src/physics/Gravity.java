package physics;

import giant_galaxy.Particle;

public class Gravity {

    double[] force(Particle particle1, Particle particle2){
        double[] force = particle1.vect(particle2);
        for (int i = 0; i < force.length; i++) {
            force[i] *= particle1.mass() * particle2.mass() / particle1.dist2(particle2.position());
        }
        return force;
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

