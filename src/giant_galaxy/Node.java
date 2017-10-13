package giant_galaxy;

import dist.BoxDist2;
import utils.Drawing;

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
     * end - inclusive last index of particles included in this node. (may not lay outside of array)
     */
    public final int start, end;
    Node lChild, rChild;
    Node parent;


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

    boolean isLeaf() {
        return !(hasLeft() || hasRight());
    }

    boolean hasLeft() {
        return lChild != null;
    }

    boolean hasRight() {
        return rChild != null;
    }

    double dist2(double[] pos){
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
            if (boxDist2.metric(pos, lChild) < r2max){
                cnt += lChild.ballwalk(pos, r2max, boxDist2);
            }
            if (boxDist2.metric(pos, rChild) < r2max){
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
            g.setColor(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
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
}
