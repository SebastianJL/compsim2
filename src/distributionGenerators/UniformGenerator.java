package distributionGenerators;

import java.util.Random;

public class UniformGenerator extends Random implements IGenerator{

    double mean;
    double standardDev;


    public UniformGenerator(){
        super();
    }

    @Override
    public double next() {
        return nextDouble();
    }
}
