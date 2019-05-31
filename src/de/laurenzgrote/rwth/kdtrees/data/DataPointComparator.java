package de.laurenzgrote.rwth.kdtrees.data;

import java.util.Comparator;

/**
 * DataPointComparator. Compares to data point according to the nth dimension
 */
public class DataPointComparator implements Comparator<DataPoint> {

    private int dim;
    
    public DataPointComparator (int dim) {
        this.dim = dim;
    }

    @Override
    public int compare(DataPoint arg0, DataPoint arg1) {
        return Double.compare(arg0.getData(dim), arg1.getData(dim));
    }

    
}