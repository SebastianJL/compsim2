package dist;

import data_structures.Node;

public class CenterOfGravityDist2 implements IMetric<Node>{

    static CenterOfGravityDist2 instance = new CenterOfGravityDist2();

    private CenterOfGravityDist2() {}

    public static CenterOfGravityDist2 getInstance() {
        return instance;
    }

    @Override
    public double metric(double[] pos, Node node) {
        double dist2 = 0;

        double[] centGrav  = node.centerOfMass;
        for(int i=0; i<node.tree.dimensions(); i++){
            dist2 += Math.pow(pos[i]-centGrav[i],2);
        }
        return dist2;
    }
}
