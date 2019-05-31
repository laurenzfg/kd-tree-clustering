package de.laurenzgrote.rwth.kdtrees.data;

import java.util.*;

/**
 * KDTreeNode
 */
public class KDTreeNode extends DataSet {
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
        if (getLength() > 10 && !split_maxdiff())
            split_variance();
    }

    private boolean split_maxdiff() {
        return false;
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
        if (maxVarianceIndex>= 0) {
            // Found a splitting dimension
            int mean = getLength() / 2; // Splitting index
            List<DataPoint> geq = getSet()[maxVarianceIndex].subList(mean, getLength() - 1);
            List<DataPoint> le = getSet()[maxVarianceIndex].subList(0, mean - 1);

            // Left is le
            left = new KDTreeNode(this, geq, tresh_maxdiff, tresh_variance);
            // Right is geq
            right = new KDTreeNode(this, le, tresh_maxdiff, tresh_variance);
        }
        // ELSE: Found no splitting dimension. Node is left as is
    }

    public KDTreeNode getLeft() {
        return left;
    }

    public KDTreeNode getRight() {
        return right;
    }
}