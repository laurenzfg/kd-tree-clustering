package de.laurenzgrote.rwth.kdtrees.data;

import java.util.ArrayList;

/**
 * TreeNode
 */
public interface TreeNode {
    ArrayList<DataPoint> getData();
    TreeNode getLeft();
    TreeNode getRight();
}