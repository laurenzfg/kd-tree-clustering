package de.laurenzgrote.rwth.kdtrees.data;

import java.util.*;

/**
 * DataSet. Immutable set of data points of the same dimension.
 * The List of Pointers is ordered by the nth dimension.
 */
public class DataSet {
    private int dim;
    private int length;
    private List<DataPoint>[] set;

    /**
     * Initialises a set of Data Points (e.g. from input). Set can not be altered.
     * Set is sorted against all features upon initialization
     * @param set List of points constituing the data set (unsorted)
     * @throws DataPointMalformatException if DataPoints are not sane
     */
    public DataSet(List<DataPoint> set) throws DataPointMalformattedException {
        this.length = set.size();
        this.dim = set.get(0).getDim();
        // All DPs must have same dim
        for (DataPoint dPoint : set)
            if (dPoint.getDim() != dim)
                throw new DataPointMalformattedException("Data Point has wrong dimension", dPoint);
        this.set = new ArrayList[dim];
        this.set[0] = set; // Save to first position
        sort(); // Sort and copy
    }

    /**
     * Creates a subset from the given set.
     * The DataSet is assumed is assumed to be complete and sorted, thus a proteced method
     * @param ds     Original Dataset
     * @param remove Datapoints to be removed
     */
    protected DataSet (DataSet ds, List<DataPoint> remove) {
        this.dim = ds.getDim();
        this.set = new ArrayList[dim];
        for (int i = 0; i < dim; i++) {
            this.set[i] = new ArrayList<>(ds.set[i]); // Copy value
            this.set[i].removeAll(remove); // Remove items to be removed
        }
        this.length = this.set[0].size();
    }

    /** 
    * Creates sorted lists for each feature.
    * Result: The nth element of the list contains all the dps sorted
    * with respect to the nth feature
    */
    private void sort () {
        // Now we sort all the arrays
        for (int cDim = 0; cDim < dim; cDim++) {
            // Comperator for the nth feature
            DataPointComparator dPointComparator = new DataPointComparator(cDim);
            if (cDim > 0) { // first is already present
                // Copy the data
                set[cDim] = new ArrayList<>(set[cDim-1]); // Copy constructor
            }
            // Sort by dim
            set[cDim].sort(dPointComparator); // Quicksort
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
        int mid = length / 2;
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
     * @return Lists with the dps ordered by the nth feature
     */
    protected List<DataPoint>[] getSet () {
        return set;
    }

    /**
     * @return DataPoints contained in the set
     */
    public List<DataPoint> getDataPoints() {
        // All the lists contain the same data
        // so we can select one randomly
        return set[0];
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
}