package data_structures;

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
    public double[] centerOfMass;
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

    /**
     * Calculate total mass of node.
     * @param node Node to calculate mass for.
     * @return Total mass
     */
    public static double mass(Node node){
        double mTot  = 0;
        for(int j = node.start; j <= node.end; j++) {
            mTot += node.tree.particles[j].mass();
        }
        return mTot;
    }

    public static double pythaDist2(double[] pos1, double[] pos2){
        double pythaDist2 = 0;
        for (int i = 0; i < pos1.length; i++) {
            pythaDist2 += Math.pow( pos1[i]-pos2[i], 2);
        }
        return pythaDist2;
    }


    public static double[] centerOfMass(Node node){
        double[] centGrav = new double[node.tree.dimensions()];
        Particle[] particles = node.tree.particles;
        double mTot = mass(node);
        for(int i=0; i < node.tree.dimensions(); i++){
            for(int j = node.start; j <= node.end; j++){
                centGrav[i] += particles[j].mass()*particles[j].position(i);
            }
            centGrav[i] /= mTot*particles.length;
        }
        return centGrav;
    }

    public static double[] CombinedCenterOfMass
            (double lMass, double rMass, double[] lCoM, double[] rCoM){
        double[] CCoM = new double[lCoM.length];
        for(int i=0; i < CCoM.length; i++){
            CCoM[i] = (lMass*lCoM[i]+rMass*rCoM[i])/(lMass+rMass);
        }
        return CCoM; //Attention: wrong definition of CoM
    }

    public static double RMaxFromCoM(double[] centerOfMass, Particle[] particles){
        double RMaxFromCoM = 0;
        for(int i=0; i<particles.length; i++){
            if(particles[i].dist2(centerOfMass)>RMaxFromCoM){
                RMaxFromCoM = particles[i].dist2(centerOfMass);
            }
        }
        return RMaxFromCoM;
    }

    public static double CombinedRMaxFromCombinedCoM(double[] lCenterOfMass, double[] rCenterOfMass, double[] CombinedCoM, double LRMax, double RRMax){
        double lDistance  = pythaDist2(lCenterOfMass, CombinedCoM);
        double rDistance  = pythaDist2(rCenterOfMass, CombinedCoM);

        if(lDistance>rDistance){
            return lDistance+LRMax;
        }
        else{
            return rDistance+RRMax;
        }
    }

    public static double[][] multiPoleM(Particle[] particles, double[] CoM){
        double[][] multiPoleM = new double[CoM.length][CoM.length];
        for (Particle particle : particles) {
            for(int j=0; j < CoM.length; j++){
                for(int k=j; k < CoM.length; k++){
                    multiPoleM[j][k] += particle.mass()*(particle.position(j)-CoM[j])*(particle.position(k)-CoM[k]);
                    multiPoleM[k][j] = multiPoleM[j][k];
                }
            }

        }
        return multiPoleM;
    }

    public static double[][] combMultiPoleM(double lMass, double[] lCoM, double[][] lMultiPoleM, double rMass, double[] rCoM, double[][] rMultiPoleM, double[] CoM) {
        double[][] combMultiPoleM = new double[CoM.length][CoM.length];
        double S_jk = 0;
        for(int j=0; j < CoM.length; j++){
            for(int k=j; k < CoM.length; k++){
                //left
                S_jk = (lCoM[j]-CoM[j])*(lCoM[k]-CoM[k]);
                combMultiPoleM[j][k] = S_jk*lMass-lMultiPoleM[j][k];

                //right
                S_jk = (rCoM[j]-CoM[j])*(rCoM[k]-CoM[k]);
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
