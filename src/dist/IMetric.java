package dist;

public interface IMetric<T> {
    public double metric(double[] pos, T obj);
}
