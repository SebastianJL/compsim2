package distributionGenerators;

public interface IGenerator {
    double next();
    void setSeed(long i);
}
