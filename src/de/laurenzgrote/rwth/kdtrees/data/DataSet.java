package de.laurenzgrote.rwth.kdtrees.data;

import java.util.HashSet;

/**
 * DataSet
 */
public class DataSet {
    private HashSet<DataPoint> set;
    private int dim;

    /**
     * Initialisez a set of Data Points
     * @param initialSize (initial number) of data points
     * @param dim dimension of *every* data point
     */
    public DataSet (int initialSize, int dim) {
        set = new HashSet<>(initialSize);
        this.dim = dim;
    }

    public void add (DataPoint d) throws DataPointMalformatException {
        if (d.getData().length != dim) {
            throw new DataPointMalformatException("Data point has wrong dim", d);
        } else {
            set.add(d);
        }
    }

    public double avg (int feature) {
        double avg = 0.0;
        for (DataPoint d : set) {
            avg += d.getData()[feature];
        }
        avg /= (double) set.size();
        return avg;
    }
}