package de.laurenzgrote.rwth.kdtrees.data;

import java.util.Collection;
import java.util.HashSet;

/**
 * KDTreeNode. A node in a KD Tree, containing data points
 */
public class KDTreeNode extends DataSet implements TreeNode {
    private int minclustersize;
    private double tresh_maxdiff, tresh_variance;
    // Children containing the subsets
    private KDTreeNode left, right;

    /**
     * Constructs KDTreeNode from List of DataPoints (used in initialization)
     * 
     * @param tresh_maxdiff  Minimum difference for a maxdiff split
     * @param tresh_variance Minimum variance for a median variance split
     */
    public KDTreeNode(Collection<DataPoint> set, double tresh_maxdiff, double tresh_variance, int minclustersize)
            throws DataPointMalformattedException {
        super(set);
        // Treshholds for splits
        this.minclustersize = minclustersize;
        this.tresh_maxdiff = tresh_maxdiff;
        this.tresh_variance = tresh_variance;
        split(); // Split node, if possible
    }

    /**
     * Splits a node if possible
     */
    private void split() {
        // First try to split by maxdiff,
        // if unsuccesful: Split by variance
        // Don't split if current cluster to small (Must be 2x Minsize)
        if (getLength() > 2*minclustersize && !split_maxdiff())
            split_variance();
    }

    /**
     * Splits the node among the maxdiff
     * 
     * @return whether maxdiff split was performed
     */
    private boolean split_maxdiff() {
        double maxDiff = -1.0; // Maximum (so Far)
        int maxDiffFeature = -1; // in which feature
        double pivot = 0.0; // placeholder
        for (int feature = 0; feature < getDim(); feature++) { // for all features
            // Split by histogram
            double min = getMinimum(feature);
            double max = getMaximum(feature);
            double range = max - min;
            // See paper
            int bins = getLength() + 1;
            double bin_width = range / ((double) bins);
            // We only need to know how many items in bin
            int[] binSizes = new int[bins]; // Java auto-initializes
            // Fill the Bins
            for (DataPoint dPoint : getSet()) {
                double value = dPoint.getData(feature);
                int bin;
                if (value == max) {
                    // Max ele would get its own bin when applying formula
                    // nonsense, so put in last bin
                    bin = bins - 1;
                } else {
                    bin = (int) Math.floor((value - min) / bin_width);
                }
                
                binSizes[bin]++;
            }

            // Assert at least 2* minclustersize size of set
            // Search Start / End for consec empty bin search
            
            int start = 0; 
            int dpsSoFar = 0;
            do {
                dpsSoFar += binSizes[start++];
            } while (dpsSoFar < minclustersize);

            dpsSoFar = 0;
            int end = getLength() - 1;
            do {
                dpsSoFar += binSizes[end--];
            } while (dpsSoFar < minclustersize);

            // Search max. consecutive empty bins
            int secondMostConsecEmpty = -100; // placeholder
            int maxConsecEmpty = -1; // placeholder
            int maxConsecBegin = 0; // placeholder

            // TODO: Write debug trace
            for (int i = start; i < end; i++) {
                int emptyFromHere = countEmptyBins(binSizes,i);
                // geq neccesary for correctness check to work
                // (otherwise multiple equal size gaps would not cause a warning)
                if (emptyFromHere >= maxConsecEmpty) {
                    secondMostConsecEmpty = maxConsecEmpty;
                    maxConsecEmpty = emptyFromHere;
                    maxConsecBegin = i;
                }
            }
            // Check if our approximation is correct
            if (secondMostConsecEmpty >= maxConsecEmpty - 1) {
                System.err.println("WARN: performed not uniquely approximated split!");
            }

            double diff = ((double) maxConsecEmpty) * bin_width;
            if (diff > maxDiff) {
                maxDiff = diff;
                maxDiffFeature = feature;
                pivot = ((double) maxConsecBegin + maxConsecEmpty) * bin_width + min;
            }
        }
        // Can a split be performed
        // Criteria A: greater than tresh; Criteria B: clusters will be large enough
        if (maxDiff > tresh_maxdiff) {
            // Yes --> Split by pivot
            splitByPivot(maxDiffFeature, pivot);
            return true;
        } else {
            return false;
        }
    }

    private int countEmptyBins (int[] binSizes, int pos) {
        int cnt = 0;
        while (binSizes[++pos] == 0 && pos < binSizes.length)
            cnt++;
        return cnt;
    }

    /**
     * Splits the node among the median of the feature with max variance
     */
    private void split_variance() {
        // Search max variance
        double maxVariance = -1.0; // Placeholder val
        int maxVarianceFeature = -1;
        for (int feature = 0; feature < getDim(); feature++) { // For all feautres
            double variance = getVariance(feature); // calc variance
            // test if over tresh and and max
            if (variance >= tresh_variance && variance > maxVariance) {
                maxVariance = variance;
                maxVarianceFeature = feature;
            }
        }
        // Can a split be performed
        if (maxVarianceFeature >= 0) {
            splitByPivot(maxVarianceFeature, getMean(maxVarianceFeature));
        }
        // ELSE: Found no splitting dimension. Node is left as is
    }

    /**
     * Splits the node at pivot
     * 
     * @param feature decisive feature
     * @param pivot   number of pivot element
     */
    private void splitByPivot(int feature, double value) {
        HashSet<DataPoint> leq = new HashSet<>(); // <=
        HashSet<DataPoint> ge = new HashSet<>(); // >
        for (DataPoint dp : getSet()) {
            if (dp.getData(feature) <= value) {
                leq.add(dp);
            } else {
                ge.add(dp);
            }
        }  
        try {
            // Left is leq pivot
            left = new KDTreeNode(leq, tresh_maxdiff, tresh_variance, minclustersize);
            // Right is ge pivot
            right = new KDTreeNode(ge, tresh_maxdiff, tresh_variance, minclustersize);
        } catch (DataPointMalformattedException e) {
            // This error cannot happen
            System.err.println("Unspecified condition: Set not sane anymore");
            // Since that case MUST NOT HAPPEN BY DESIGN
            // we dont't do exception handling, we just quit.
            System.exit(-1);
        }
        
    }

    /**
     * @return Left child
     */
    public KDTreeNode getLeft() {
        return left;
    }

    /**
     * @return Right child
     */
    public KDTreeNode getRight() {
        return right;
    }
}