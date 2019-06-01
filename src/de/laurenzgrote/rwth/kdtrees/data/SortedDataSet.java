package de.laurenzgrote.rwth.kdtrees.data;

import java.util.*;

/**
 * DataSet. Immutable set of data points of the same dimension.
 * The List of Pointers is ordered by the nth dimension.
 */
public class SortedDataSet extends DataSet{
    
    private List<DataPoint>[] sortedSet;

    /**
     * Initialises a set of Data Points (e.g. from input). Set can not be altered.
     * Set is sorted against all features upon initialization!
     * @param set List of points constituing the data set (unsorted)
     * @throws DataPointMalformatException if DataPoints are not sane
     */
    public SortedDataSet(List<DataPoint> set) throws DataPointMalformattedException {
        super(set);
        this.sortedSet = new ArrayList[getDim()];
        this.sortedSet[0] = set; // Save to first position
        sort(); // Sort and copy
    }

    /**
     * Creates a subset from the given set.
     * Set: ds / remove
     * The DataSet is assumed is assumed to be complete and sorted, thus a proteced method
     * @param ds     Original Dataset
     * @param remove Datapoints to be removed
     */
    protected SortedDataSet (SortedDataSet ds, List<DataPoint> remove) {
        super(ds, remove); // Initialize "normal" sets
        this.sortedSet = new ArrayList[getDim()];
        for (int i = 0; i < getDim(); i++) {
            this.sortedSet[i] = new ArrayList<>(ds.sortedSet[i]); // Copy value
            this.sortedSet[i].removeAll(remove); // Remove items to be removed
        }
    }

    /** 
    * Creates sorted lists for each feature.
    * Result: The nth element of the list contains all the dps sorted
    * with respect to the nth feature
    */
    private void sort () {
        // Now we sort all the arrays
        for (int cDim = 0; cDim < getDim(); cDim++) {
            // Comperator for the nth feature
            DataPointComparator dPointComparator = new DataPointComparator(cDim);
            if (cDim > 0) { // first is already present
                // Copy the data
                sortedSet[cDim] = new ArrayList<>(sortedSet[cDim-1]); // Copy constructor
            }
            // Sort by dim
            sortedSet[cDim].sort(dPointComparator); // Quicksort
        }
    }

    /**
     * @return Lists with the dps ordered by the nth feature
     */
    protected List<DataPoint>[] getSortedSet () {
        return sortedSet;
    }
}