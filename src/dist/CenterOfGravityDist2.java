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

        double[] centGrav  = node.centerOfGravity;
        for(int i=0; i<node.tree.dimensions(); i++){
            dist2 += Math.pow(pos[i]-centGrav[i],2);
        }
        return dist2;
    }
}
