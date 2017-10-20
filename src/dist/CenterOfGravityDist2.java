package dist;

import giant_galaxy.Node;
import giant_galaxy.Particle;

import java.util.Arrays;

public class CenterOfGravityDist2 implements IMetric<Node>{

    static CenterOfGravityDist2 instance = new CenterOfGravityDist2();

    private CenterOfGravityDist2() {}

    public static CenterOfGravityDist2 getInstance() {
        return instance;
    }

    @Override
    public double metric(double[] pos, Node node) {
        double dist2 = 0;

        double[] centGrav  = centerOfGravity(Arrays.copyOfRange(node.tree.particles, node.start, node.end+1),
                node.tree.dimensions());
        for(int i=0; i<node.tree.dimensions(); i++){
            dist2 += Math.pow(pos[i]-centGrav[i],2);
        }
        return dist2;
    }

    public double mass(Particle[] particles){
        double mTot  = 0;
        for(int j=0; j<particles.length; j++) {
            mTot += particles[j].mass();
        }
        return mTot;
    }

    public double pythaDist2(double[] pos1, double[] pos2){
        double pythaDist2 = 0;
        for (int i=0; i<pos1.length; i++) {
            pythaDist2 += Math.pow( pos1[i]-pos2[i], 2);
        }
        return pythaDist2;
    }


    public double[] centerOfGravity(Particle[] particles, int dimensions){
        double[] centGrav = new double[dimensions];
        double mTot = mass(particles);
        for(int i=0; i<dimensions; i++){
            for(int j=0; j<particles.length; j++){
                centGrav[i] += particles[j].mass()*particles[j].position(i);
            }
            centGrav[i] /= mTot*particles.length;
        }
        return centGrav;
    }

    public double[] CombinedCenterOfGravity
            (double lMass, double rMass, double[] lCoG, double[] rCoG){
        return [1,2,3] //Attention: wrong definition of CoG
    }

    public double RMaxFromCoG(double[] centerOfGravity, Particle[] particles){
        double RMaxFromCoG = 0;
        for(int i=0; i<particles.length; i++){
            if(particles[i].dist2(centerOfGravity)>RMaxFromCoG){
                RMaxFromCoG = particles[i].dist2(centerOfGravity);
            }
        }
        return RMaxFromCoG;
    }

    public double CombinedRMaxFromCombinedCoG(double[] lCenterOfGravity, double[] rCenterOfGravity, double[] CombinedCoG, double LRMax, double RRMax){
        double lDistance  = pythaDist2(lCenterOfGravity, CombinedCoG);
        double rDistance  = pythaDist2(rCenterOfGravity, CombinedCoG);

        if(lDistance>rDistance){
            return lDistance+LRMax;
        }
        else{
            return rDistance+RRMax;
        }
    }
}
