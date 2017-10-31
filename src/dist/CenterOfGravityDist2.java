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
        double[] CCoG = new double[lCoG.length];
        for(int i=0; i < CCoG.length; i++){
            CCoG[i] = (lMass*lCoG[i]+rMass*rCoG[i])/(lMass+rMass);
        }
        return CCoG; //Attention: wrong definition of CoG
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

    public double[][] multiPoleM(Particle[] particles, double[] CoG){
        double[][] multiPoleM = new double[CoG.length][CoG.length];
        for (Particle particle : particles) {
            for(int j=0; j < CoG.length; j++){
                for(int k=j; k < CoG.length; k++){
                    multiPoleM[j][k] += particle.mass()*(particle.position(j)-CoG[j])*(particle.position(k)-CoG[k]);
                    multiPoleM[k][j] = multiPoleM[j][k];
                }
            }

        }
        return multiPoleM;
    }

    public double[][] combMultiPoleM(double lMass, double[] lCoG, double[][] lMultiPoleM, double rMass, double[] rCoG, double[][] rMultiPoleM, double[] CoG) {
        double[][] combMultiPoleM = new double[CoG.length][CoG.length];
        double S_jk = 0;
        for(int j=0; j < CoG.length; j++){
            for(int k=j; k < CoG.length; k++){
                //left
                S_jk = (lCoG[j]-CoG[j])*(lCoG[k]-CoG[k]);
                combMultiPoleM[j][k] = S_jk*lMass-lMultiPoleM[j][k];

                //right
                S_jk = (rCoG[j]-CoG[j])*(rCoG[k]-CoG[k]);
                combMultiPoleM[j][k] += S_jk*rMass-rMultiPoleM[j][k];

                //symmetric!
                combMultiPoleM[k][j] = combMultiPoleM[j][k];
            }
        }
        return combMultiPoleM;
    }

    public double trace(double[][] multMoment){
        double trace = 0;
        for (int i =0; i<multMoment[0].length;i++){
            trace += multMoment[i][i];
        }
        return trace;
    }


}
