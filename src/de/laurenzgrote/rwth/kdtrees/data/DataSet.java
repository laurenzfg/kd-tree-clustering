package de.laurenzgrote.rwth.kdtrees.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * DataSet. Set of Data Points. Class supports different statistical queries.
 */
public class DataSet {
    // This is a "magic number"
    // I did not make it a parameter bcause 5% proved good
    // and is also proposed in the original paper
    private static double mincluster = 0.05;
    protected int minclustersize;

    private int dim;
    private int length;

    private HashSet<DataPoint> set;

    /**
     * Initialises a set of Data Points (e.g. from input). Set can not be altered.
     * @param set List of points constituing the data set
     * @throws DataPointMalformatException if DataPoints are not sane
     */
    public DataSet(Collection<DataPoint> set) throws DataPointMalformattedException {
        this.length = set.size();
        this.dim = set.iterator().next().getDim();
        // All DPs must have same dim
        for (DataPoint dPoint : set)
            if (dPoint.getDim() != dim)
                throw new DataPointMalformattedException("Data Point has wrong dimension", dPoint);
        // set is sane
        this.set = new HashSet<>(set);
        // Treshholds for splits
        this.minclustersize = (int) (mincluster * getLength());
    }

    /**
     * Creates a subset from the given set.
     * Set: ds / remove
     * The DataSet is assumed is assumed to be complete and sorted, thus a proteced method.
     * Only needed for the SortedDataSet implementation
     * @param ds     Original Dataset
     * @param remove Datapoints to be removed
     */
    protected DataSet (DataSet ds, Collection<DataPoint> remove) {
        this.set = new HashSet<>(ds.set);
        this.set.removeAll(remove);
        this.length = set.size();
        this.dim = ds.getDim();
        // Treshholds for splits
        this.minclustersize = (int) (mincluster * getLength());
    }

    /**
     * @return Dimension of the every data point
     */
    public int getDim() {
        return dim;
    }

    /**
     * @return number of vectors in the data set
     */
    public int getLength() {
        return length;
    }

    /**
     * @param feature Feature currently looked at
     * @return Average value in the set of the feature
     */
    public double getAvg (int feature) {
        double avg = 0.0;
        for (DataPoint d : set) {
            avg += d.getData(feature);
        }
        avg /= (double) length;
        return avg;
    }

    /**
     * @param feature Feature currently looked at
     * @return Median value in the set of the feature
     */
    public double getMean (int feature) {
        // Set to array (sets cannot be sorted)
        DataPoint[] array = set.toArray(new DataPoint[0]);
        Arrays.sort(array, new DataPointComparator(feature));
        int mid = length / 2;
        return array[mid].getData(feature);
    }

    /**
     * @param feature Feature currently looked at
     * @return Minimum value in the set of the feature
     */
    public double getMinimum (int feature) {
        double minimum = 1; // normalized, highest possible
        for (DataPoint dPoint : set) {
            double val = dPoint.getData(feature);
            if (val < minimum) {
                minimum = val;
            }
        }
        return minimum;
    }

    /**
     * @param feature Feature currently looked at
     * @return Maximum value in the set of the feature
     */
    public double getMaximum (int feature) {
        double maximum = 0; // normalized, lowest possible
        for (DataPoint dPoint : set) {
            double val = dPoint.getData(feature);
            if (val > maximum) {
                maximum = val;
            }
        }
        return maximum;
    }
    
    /**
     * @param feature Feature currently looked at
     * @return variance of the given feature
     * Formula: https://doi.org/10.1002/0471667196.ess2516.pub2
     * (but no root since we look for variance not stddev)
     */
    public double getVariance (int feature) {
        double avg = getAvg(feature);
        double variance = 0.0;
        for (DataPoint d : set) {
            double val = d.getData(feature);
            variance += Math.pow(val - avg,2);
        }
        variance /= (double) (length - 1);
        return variance;
    }

    /**
     * @param feature Feature currently looked at
     * @return stdev of the given feature
     */
    public double getStddev (int feature) {
        double variance = getVariance(feature);
        return Math.sqrt(variance);
    }

    /**
     * @return DataPoints contained in the set
     */
    public Set<DataPoint> getSet() {
        // All the lists contain the same data
        // so we can select one randomly
        return set;
    }
}