package de.laurenzgrote.rwth.kdtrees.data;

import java.util.Collection;
import java.util.HashSet;

/**
 * MedianKDTreeNode. A node in a KD Tree, containing data points
 */
public class MedianKDTreeNode extends DataSet implements TreeNode {
    private double tresh_maxdiff, tresh_variance;
    // Children containing the subsets
    private MedianKDTreeNode left, right;

    /**
     * Constructs MedianKDTreeNode from List of DataPoints (used in initialization)
     * 
     * @param tresh_maxdiff  Minimum difference for a maxdiff split
     * @param tresh_variance Minimum variance for a median variance split
     */
    public MedianKDTreeNode(Collection<DataPoint> set, double tresh_maxdiff, double tresh_variance)
            throws DataPointMalformattedException {
        super(set);
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
        if (getLength() > 2*minclustersize)
            split_variance();
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
            left = new MedianKDTreeNode(leq, tresh_maxdiff, tresh_variance);
            // Right is ge pivot
            right = new MedianKDTreeNode(ge, tresh_maxdiff, tresh_variance);
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
    public MedianKDTreeNode getLeft() {
        return left;
    }

    /**
     * @return Right child
     */
    public MedianKDTreeNode getRight() {
        return right;
    }
}