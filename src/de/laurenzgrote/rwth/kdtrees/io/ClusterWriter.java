package de.laurenzgrote.rwth.kdtrees.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import de.laurenzgrote.rwth.kdtrees.data.DataPoint;
import de.laurenzgrote.rwth.kdtrees.data.TreeNode;

/**
 * ClusterWriter
 */
public abstract class ClusterWriter {

    public static void writeToGnuplot(TreeNode tNode, Path path){
        StringBuilder outString = new StringBuilder();

        // First we make an ArrayList of just the leaf nodes
        // since these contain the clusters
        // We get them by DFS using recursion
        ArrayList<TreeNode> leafs = getLeafs(tNode);
        System.out.println("Cluster count: " + leafs.size());

        // Now we do the output
        for (int i = 0; i < leafs.size(); i++) {
            ArrayList<DataPoint> leaf = leafs.get(i).getData();
            for (DataPoint dPoint : leaf) {
                outString.append(dPoint); // toString() of dPoint
                outString.append(i);
                outString.append('\n');
            }
        }

        // File I/O
        try {
        // if present, overwrite
            if (Files.exists(path))
                Files.delete(path);
            Files.write(path, outString.toString().getBytes());
            
        } catch (IOException e) {
            System.err.println("Couldn't write to File: " + path.toAbsolutePath());
        }
        // Since Java new I/O is new, fresh and hipster, I do not need to close file
    }

    private static ArrayList<TreeNode> getLeafs(TreeNode tNode) {
        ArrayList<TreeNode> leafs = new ArrayList<>();;
        if (tNode.getLeft() == null && tNode.getRight() == null) {
            leafs.add(tNode);
        } else if (tNode.getLeft() != null && tNode.getRight() != null) {
            leafs.addAll(getLeafs(tNode.getLeft()));
            leafs.addAll(getLeafs(tNode.getRight()));
        } else {
            System.err.println("Unspecified condition: TreeNode with one child");
            // Since that case MUST NOT HAPPEN BY DESIGN
            // we dont't do exception handling, we just quit.
            // Stil better than an NPE :D
            System.exit(-1);
        }
        return leafs;
    }
}