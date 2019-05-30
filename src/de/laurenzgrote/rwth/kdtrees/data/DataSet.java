package de.laurenzgrote.rwth.kdtrees.data;

import java.util.ArrayList;

/**
 * ImmutableDataSet. Set of data points of the same dimension.
 * The List of Pointers is ordered by the nth dimension.
 */
public class DataSet {
    private int dim;
    private ArrayList<DataPoint>[] set;

    /**
     * Initialises a set of Data Points (e.g. from input).
     * Set can not be altered.
     * @param set List of points constituing the data set (unsorted)
     */
    public DataSet (ArrayList<DataPoint> set) {
        dim = set.get(0).getDim();
        this.set = new ArrayList[dim];
        this.set[0] = set;

        sort();
    }

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

    public double avg (int feature) {
        double avg = 0.0;
        for (DataPoint d : set[0]) {
            avg += d.getData(feature);
        }
        avg /= (double) set[0].size();
        return avg;
    }

    /**
     * @return Dimension of the IDS
     */
    public int getDim() {
        return dim;
    }
}