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

    double[] centerOfGravity(Particle[] particles, int dimensions){
        double[] centGrav = new double[dimensions];
        for(int i=0; i<dimensions; i++){
            for(int j=0; j<particles.length; j++){
                centGrav[i] += particles[j].position(i);
            }
            centGrav[i] /= particles.length;
        }
        return centGrav;
    }
}
