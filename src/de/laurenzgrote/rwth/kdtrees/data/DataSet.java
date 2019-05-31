package de.laurenzgrote.rwth.kdtrees.data;

import java.util.ArrayList;

/**
 * ImmutableDataSet. Set of data points of the same dimension.
 * The List of Pointers is ordered by the nth dimension.
 */
public class DataSet {
    private int dim;
    private int length;
    private ArrayList<DataPoint>[] set;

    /**
     * Initialises a set of Data Points (e.g. from input). Set can not be altered.
     * 
     * @param set List of points constituing the data set (unsorted)
     * @throws DataPointMalformatException
     */
    public DataSet(ArrayList<DataPoint> set) throws DataPointMalformatException {
        this.dim = set.get(0).getDim();
        this.length = set.size();
        this.set = new ArrayList[dim];
        this.set[0] = set;
        isSane();
        sort();
    }

    /**
     * Checks if all data points have the same dimension
     * 
     * @throws DataPointMalformatException
     */
    private void isSane() throws DataPointMalformatException {
        for (DataPoint dPoint : this.set[0])
            if (dPoint.getDim() != dim)
                throw new DataPointMalformatException("Data Point has wrong dimension", dPoint);
    }

    /** 
    * Sorts all the array fields
    * Result: The nth element of the set contains all the dps sorted
    * with respect to the nth feature
    */
    private void sort () {
        // Now we sort all the arrays
        for (int cDim = 0; cDim < dim; cDim++) {
            DataPointComparator dPointComparator = new DataPointComparator(cDim);
            if (cDim > 0) {
                // Copy the data
                set[cDim] = new ArrayList<>(set[cDim-1]);
            }
            // Sort by dim
            set[cDim].sort(dPointComparator);
        }
    }

    /**
     * @param feature Feature currently looked at
     * @return Average value in the set of the feature
     */
    public double getAvg (int feature) {
        double avg = 0.0;
        for (DataPoint d : set[0]) {
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
        // Arrays are sourted
        int mid = set[0].size() / 2;
        return set[feature].get(mid).getData(feature);
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
        for (int i = 0; i < dim; i++) {
            double val = set[feature].get(i).getData(feature);
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
     * @return Dimension of the IDS
     */
    public int getDim() {
        return dim;
    }
}