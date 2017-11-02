package dist;

import data_structures.Node;

public class BoxDist2 implements IMetric<Node> {

    static BoxDist2 instance = new BoxDist2();

    private BoxDist2() {}

    public static BoxDist2 getInstance() {
        return instance;
    }

    @Override
    public double metric(double[] pos, Node node){
        double dist2 = 0;
        for (int i = 0; i < pos.length; i++){
            if (pos[i] < node.posMin[i])
            {dist2 += Math.pow(node.posMin[i] - pos[i], 2); }
            else if (pos[i] > node.posMax[i])
            {dist2 += Math.pow(node.posMax[i] - pos[i], 2); }
            else
            {dist2 += 0; }
        }
        return dist2;
    }
}
