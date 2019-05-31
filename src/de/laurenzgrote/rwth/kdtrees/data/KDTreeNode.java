package de.laurenzgrote.rwth.kdtrees.data;

import java.util.*;

/**
 * KDTreeNode
 */
public class KDTreeNode extends DataSet implements TreeNode{
    private double tresh_maxdiff, tresh_variance;
    private KDTreeNode left, right;

    public KDTreeNode (ArrayList<DataPoint> set, double tresh_maxdiff, double tresh_variance) throws DataPointMalformatException {
        super(set);
        this.tresh_maxdiff = tresh_maxdiff;
        this.tresh_variance = tresh_variance;
        split(); // Split node, if possible
    }

    public KDTreeNode (DataSet set, List<DataPoint> remove, double tresh_maxdiff, double tresh_variance) {
        super(set, remove);
        this.tresh_maxdiff = tresh_maxdiff;
        this.tresh_variance = tresh_variance;
        split(); // Split node, if possible
    }

    private void split() {
        // First try to split by maxdiff,
        // if unsuccesful: Split by variance
        if (getLength() > 100 && !split_maxdiff())
            split_variance();
    }

    private boolean split_maxdiff() {
        // Global Maxdiff
        double maxDiff = -1.0;
        int maxDiffFeature = -1;
        int pivot = -1;
        for (int feature = 0; feature < getDim(); feature++) {
            ArrayList<DataPoint> sortedList = getSet()[feature]; // Sorted by feature
            for (int e = 0; e < getLength() - 1; e++) {
                double diff = sortedList.get(e+1).getData(feature) - sortedList.get(e).getData(feature);
                if (diff > maxDiff) {
                    maxDiff = diff;
                    maxDiffFeature = feature;
                    pivot = e;
                }
            }
        }
        if (maxDiff > tresh_maxdiff) {
            splitByPivot(maxDiffFeature, pivot);
            return true;
        } else {
            return false;
        }
    }

    private void split_variance() {
        // Search max Variance geq tresh (if present)
        double maxVariance = -1.0; // Placeholder val
        int maxVarianceIndex = -1;
        for (int i = 0; i < getDim(); i++) {
            double variance = getVariance(i);
            if (variance >= tresh_variance && variance > maxVariance) {
                maxVariance = variance;
                maxVarianceIndex = i;
            }
        }
        if (maxVarianceIndex >= 0) {
            // Found a splitting dimension
            int mean = getLength() / 2; // Splitting index
            splitByPivot(maxVarianceIndex, mean);
        }
        // ELSE: Found no splitting dimension. Node is left as is
    }

    private void splitByPivot(int feature, int pivot) {
        // BE AWARE: FROM is inclusive, TO is exclusive
        List<DataPoint> ge = getSet()[feature].subList(pivot + 1, getLength());
        List<DataPoint> leq = getSet()[feature].subList(0, pivot + 1);

        // Left is leq
        left = new KDTreeNode(this, ge, tresh_maxdiff, tresh_variance);
        // Right is ge
        right = new KDTreeNode(this, leq, tresh_maxdiff, tresh_variance);
    }

    public KDTreeNode getLeft() {
        return left;
    }

    public KDTreeNode getRight() {
        return right;
    }
}