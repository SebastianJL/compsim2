package distributionGenerators;

import java.util.Random;

public class UniformGenerator extends Random implements IGenerator{

    double mean;
    double standardDev;


    public UniformGenerator(){
        super();
    }

    public double nextPos() {
        return nextDouble();
    }

    public double nextVel() { return nextDouble(); }

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
