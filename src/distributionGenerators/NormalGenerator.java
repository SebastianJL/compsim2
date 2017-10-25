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
        this.mean = 0.8;
        this.standardDev = 0.06;
    }


    @Override
    public double next(){
        double rValue = nextGaussian()*standardDev+mean;
        if(rValue > 0 && rValue < 1){
            return rValue;
        }
        else{
            return next();
        }
    }
}
