package de.laurenzgrote.rwth.kdtrees.data;

import java.util.Set;

/**
 * TreeNode. Interface of an binary tree containing clusters.
 * Leafs are the final clusters of the root node.
 * So the sum of the childs are the nodes own data,
 * no element is shared between the child nodes.
 */
public interface TreeNode {
    // List with all Data Points in Node
    Set<DataPoint> getSet();
    // Children of current node
    TreeNode getLeft();
    TreeNode getRight();

    // Summary statistics
    int getDim();
    int getLength();
    double getAvg (int feature);
    double getMean (int feature);
    double getMinimum (int feature);
    double getMaximum (int feature);
    double getVariance (int feature);
    double getStddev (int feature);
}