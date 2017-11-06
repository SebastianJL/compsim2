package distributionGenerators;

import java.util.Random;

public class NormalGenerator extends Random implements IGenerator{

    double mean;
    double standardDev;



    public NormalGenerator(double mean, double standardDev){
        super();
        this.mean = mean;
        this.standardDev = standardDev;
    }

    public NormalGenerator(){
        super();
        this.mean = 0.5;
        this.standardDev = 0.1;
    }


    public double nextPos(){
        double rValue = nextGaussian()*standardDev+mean;
        if(rValue > 0 && rValue < 1){
            return rValue;
        }
        else{
            return nextPos();
        }
    }

    public double nextVel(){
        return nextGaussian()*standardDev+mean;
    }

    public int nextDirection(){
        boolean bool = nextBoolean();
        if(bool==true){
            return 1;
        }
        else{
            return -1;
        }
    }
}
