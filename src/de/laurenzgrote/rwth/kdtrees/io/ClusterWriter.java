package de.laurenzgrote.rwth.kdtrees.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.laurenzgrote.rwth.kdtrees.data.DataPoint;
import de.laurenzgrote.rwth.kdtrees.data.TreeNode;

/**
 * ClusterWriter. Writes clustering data to various output formats
 */
public abstract class ClusterWriter {

    /**
     * Writes clustering data to gnuplot compatible data
     * 
     * @param tNode Root node
     * @param path  Output path
     * @throws FileMalformattedException
     */
    public static void writeToGnuplot(TreeNode tNode, Path path) throws FileMalformattedException {
        // Only applicable to 2D data
        if (tNode.getDim() != 2)
            throw new FileMalformattedException(path, "Can't write Dataset as dim not equal 2");

        StringBuilder outString = new StringBuilder(); // Output buffer

        // First we make an ArrayList of just the leaf nodes
        // since these contain the clusters
        // We get them by DFS using recursion
        List<TreeNode> leafs = getLeafs(tNode);
        System.out.println("Cluster count: " + leafs.size());

        // Now we do the output
        for (int i = 0; i < leafs.size(); i++) {
            // FOR every leaf
            Set<DataPoint> leaf = leafs.get(i).getSet();
            for (DataPoint dPoint : leaf) {
                outString.append(dPoint); // toString() of dPoint
                outString.append(i); // Clustering info
                outString.append('\n');
            }
        }

        // File I/O (Writing with New IO)
        try {
        // if present, overwrite
            if (Files.exists(path))
                Files.delete(path);
            // Copy profile for gnuplot
            if (leafs.size() > 64) {
                Files.copy(Paths.get("profile_large.gp"), path);
            } else {
                Files.copy(Paths.get("profile.gp"), path);
            }
            Files.write(path, outString.toString().getBytes(), StandardOpenOption.APPEND);
            
        } catch (IOException e) {
            System.err.println("Couldn't write to File: " + path.toAbsolutePath());
        }
        // Since Java new I/O is new, fresh and hipster, I do not need to close file
    }

    /**
     * DFS which extracts leaf nodes from binary tree
     */
    private static List<TreeNode> getLeafs(TreeNode tNode) {
        // Assumption: Tree is either leaf or has two children
        List<TreeNode> leafs = new ArrayList<>();
        if (tNode.getLeft() == null && tNode.getRight() == null) {
            // Leaf node
            leafs.add(tNode);
        } else if (tNode.getLeft() != null && tNode.getRight() != null) {
            // Two children
            leafs.addAll(getLeafs(tNode.getLeft()));
            leafs.addAll(getLeafs(tNode.getRight()));
        } else {
            // One child
            System.err.println("Unspecified condition: TreeNode with one child");
            // Since that case MUST NOT HAPPEN BY DESIGN
            // we dont't do exception handling, we just quit.
            // Still better than an NPE :D
            System.exit(-1);
        }
        return leafs;
    }
}