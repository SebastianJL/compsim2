package giant_galaxy;

import dist.BoxDist2;
import utils.Drawing;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.Random;

/**
 * Basic Container for the BinaryTree.
 */
@SuppressWarnings("SpellCheckingInspection")
public class Node {
    public final BinaryTree tree;
    /**
     *
     */
    public final double[] posMin, posMax;
    /**
     * start - first index
     * end - inclusive last index of indices included in this node. (may not lay outside of array)
     */
    public final int start, end;
    public double[] centerOfGravity;
    public double RMax;
    public double mass;
    public double trace;
    public double[][] multMoment;
    public Node lChild, rChild;
    public Node parent;



    Node(double[] posMin, double[] posMax, int start, int end, Node parent, BinaryTree binaryTree) {
        this.tree = binaryTree;
        assert posMax.length == posMin.length;
        this.posMin = posMin;
        this.posMax = posMax;
        this.start = start;
        this.end = end;
        this.parent = parent;
    }


    boolean isLeftChild() {
        return this == parent.lChild;
    }

    public boolean isLeaf() {
        return !(hasLeft() || hasRight());
    }

    public boolean contains(int pNumber) { return (pNumber >= start && pNumber <= end); }

    public boolean hasLeft() {
        return lChild != null;
    }

    public boolean hasRight() {
        return rChild != null;
    }

    public double dist2(double[] pos){
        double dist2 = 0;
        for (int i = 0; i < pos.length; i++){
            if (pos[i] < posMin[i])
                {dist2 += Math.pow(posMin[i] - pos[i], 2); }
            else if (pos[i] > posMax[i])
                {dist2 += Math.pow(posMax[i] - pos[i], 2); }
            else
                {dist2 += 0; }
        }
        return dist2;
    }

    int ballwalk(double[] pos, double r2max, BoxDist2 boxDist2) {
        int cnt = 0;
        if (isLeaf()) {
            for (int j = start; j <= end; j++){
                if (tree.particles[j].dist2(pos) < r2max){
                    cnt++;
                }
            }
        }
        else {
            if (hasLeft() && boxDist2.metric(pos, lChild) < r2max){
                cnt += lChild.ballwalk(pos, r2max, boxDist2);
            }
            if (hasRight() && boxDist2.metric(pos, rChild) < r2max){
                cnt += rChild.ballwalk(pos, r2max, boxDist2);
            }
        }
        return cnt;
    }

    void paint(Graphics g, double scale) {
        if (isLeaf()) {
            double x = posMin[0] * scale;
            double y = posMin[1] * scale;
            double width = (posMax[0] - posMin[0]) * scale;
            double height = (posMax[1] - posMin[1]) * scale;
            Rectangle scaledValues = Drawing.transform(posMin[0], posMin[1], posMax[0] - posMin[0],
                    posMax[1] - posMin[1], scale);
            g.setColor(Color.BLACK);
            g.drawRect(scaledValues.x, scaledValues.y, scaledValues.width, scaledValues.height);
            Random rand = new Random();
//            g.setColor(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
            for (int i = start; i <= end; i++) {
                tree.particles[i].paint(g, scale, 7);
            }
        } else {
            if (hasLeft()) {
                lChild.paint(g, scale);
            }
            if (hasRight()) {
                rChild.paint(g, scale);
            }
        }
    }

    void buildTreeImage(DefaultMutableTreeNode parent){
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("start = " +start+", end= "+end);
        parent.add(node);
        if(hasLeft()) {
            lChild.buildTreeImage(node);
        }
        if (hasRight()) {
            rChild.buildTreeImage(node);
        }
    }

    public static double mass(Particle[] particles){
        double mTot  = 0;
        for(int j=0; j<particles.length; j++) {
            mTot += particles[j].mass();
        }
        return mTot;
    }

    public static double pythaDist2(double[] pos1, double[] pos2){
        double pythaDist2 = 0;
        for (int i=0; i<pos1.length; i++) {
            pythaDist2 += Math.pow( pos1[i]-pos2[i], 2);
        }
        return pythaDist2;
    }


    public static double[] centerOfGravity(Particle[] particles, int dimensions){
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

    public static double[] CombinedCenterOfGravity
            (double lMass, double rMass, double[] lCoG, double[] rCoG){
        double[] CCoG = new double[lCoG.length];
        for(int i=0; i < CCoG.length; i++){
            CCoG[i] = (lMass*lCoG[i]+rMass*rCoG[i])/(lMass+rMass);
        }
        return CCoG; //Attention: wrong definition of CoG
    }

    public static double RMaxFromCoG(double[] centerOfGravity, Particle[] particles){
        double RMaxFromCoG = 0;
        for(int i=0; i<particles.length; i++){
            if(particles[i].dist2(centerOfGravity)>RMaxFromCoG){
                RMaxFromCoG = particles[i].dist2(centerOfGravity);
            }
        }
        return RMaxFromCoG;
    }

    public static double CombinedRMaxFromCombinedCoG(double[] lCenterOfGravity, double[] rCenterOfGravity, double[] CombinedCoG, double LRMax, double RRMax){
        double lDistance  = pythaDist2(lCenterOfGravity, CombinedCoG);
        double rDistance  = pythaDist2(rCenterOfGravity, CombinedCoG);

        if(lDistance>rDistance){
            return lDistance+LRMax;
        }
        else{
            return rDistance+RRMax;
        }
    }

    public static double[][] multiPoleM(Particle[] particles, double[] CoG){
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

    public static double[][] combMultiPoleM(double lMass, double[] lCoG, double[][] lMultiPoleM, double rMass, double[] rCoG, double[][] rMultiPoleM, double[] CoG) {
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

    public static double trace(double[][] multMoment){
        double trace = 0;
        for (int i =0; i<multMoment[0].length;i++){
            trace += multMoment[i][i];
        }
        return trace;
    }
}
