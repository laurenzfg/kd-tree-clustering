package de.laurenzgrote.rwth.kdtrees.data;

import java.util.*;

/**
 * KDTreeNode. A node in a KD Tree, containing data points
 */
public class KDTreeNode extends DataSet implements TreeNode{
    private int minclustersize = 100; // TODO must be a calculated value
    private double tresh_maxdiff, tresh_variance;
    // Children containing the subsets
    private KDTreeNode left, right;

    /**
     * Constructs KDTreeNode from List of DataPoints
     * @param tresh_maxdiff Minimum difference for a maxdiff split
     * @param tresh_variance Minimum variance for a median variance split
     */
    public KDTreeNode (List<DataPoint> set, double tresh_maxdiff, double tresh_variance) throws DataPointMalformattedException {
        super(set);
        // Treshholds for splits
        this.tresh_maxdiff = tresh_maxdiff;
        this.tresh_variance = tresh_variance;
        split(); // Split node, if possible
    }

    /**
     * Constructs KDTreeNode as a subset of a given set
     * (set / remove)
     * @param tresh_maxdiff Minimum difference for a maxdiff split
     * @param tresh_variance Minimum variance for a median variance split
     */
    public KDTreeNode (DataSet set, List<DataPoint> remove, double tresh_maxdiff, double tresh_variance) {
        super(set, remove);
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
        // Don't split if current cluster to small
        if (getLength() > minclustersize && !split_maxdiff())
            split_variance();
    }

    /**
     * Splits the node among the maxdiff
     * @return whether maxdiff split was performed 
     */
    private boolean split_maxdiff() {
        double maxDiff = -1.0; // Maximum (so Far)
        int maxDiffFeature = -1; // in which feature
        int pivot = -1; // which element acts as pivot
        for (int feature = 0; feature < getDim(); feature++) { // for all features
            List<DataPoint> sortedList = getSet()[feature]; // DPs sorted by feature
            for (int i = 0; i < getLength() - 1; i++) { // for all DPs
                // Calculate diff
                double diff = sortedList.get(i+1).getData(feature) - sortedList.get(i).getData(feature);
                if (diff > maxDiff) { // Compare if max
                    maxDiff = diff;
                    maxDiffFeature = feature;
                    pivot = i;
                }
            }
        }
        // Can a split be performed
        // Criteria A: greater than tresh; Criteria B: clusters will be large enough
        if (maxDiff > tresh_maxdiff && pivot >= minclustersize && getLength() - pivot >= minclustersize) {
            // Yes --> Split by pivot
            splitByPivot(maxDiffFeature, pivot);
            return true;
        } else {
            return false;
        }
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
            // Found feature with sufficently high variance
            int mean = getLength() / 2; // Pivot
            splitByPivot(maxVarianceFeature, mean);
        }
        // ELSE: Found no splitting dimension. Node is left as is
    }

    /**
     * Splits the node at pivot
     * @param feature decisive feature
     * @param pivot number of pivot element
     */
    private void splitByPivot(int feature, int pivot) {
        // Lists of ge and leq elements
        // BE AWARE: FROM is inclusive, TO is exclusive
        List<DataPoint> ge = getSet()[feature].subList(pivot + 1, getLength());
        List<DataPoint> leq = getSet()[feature].subList(0, pivot + 1);

        // Left is leq pivot
        left = new KDTreeNode(this, ge, tresh_maxdiff, tresh_variance);
        // Right is ge pivot
        right = new KDTreeNode(this, leq, tresh_maxdiff, tresh_variance);
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