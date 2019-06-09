package de.laurenzgrote.rwth.kdtrees;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import de.laurenzgrote.rwth.kdtrees.data.DataPoint;
import de.laurenzgrote.rwth.kdtrees.data.DataPointMalformattedException;
import de.laurenzgrote.rwth.kdtrees.data.KDTreeNode;
import de.laurenzgrote.rwth.kdtrees.data.TreeNode;
import de.laurenzgrote.rwth.kdtrees.io.ClusterWriter;
import de.laurenzgrote.rwth.kdtrees.io.DataSetFactory;
import de.laurenzgrote.rwth.kdtrees.io.FileMalformattedException;

public class Main {

    public static void main(String[] args) {
        Path path = Paths.get(args[0]);
        double tresh_maxdiff = Double.parseDouble(args[1]);
        double tresh_variance = Double.parseDouble(args[2]);
        try {
            List<DataPoint> dPoints = DataSetFactory.readFromDenseMatrix(path);
            TreeNode dSet = new KDTreeNode(dPoints, tresh_maxdiff, tresh_variance);
            ClusterWriter.writeToGnuplot(dSet, Paths.get(args[3]));
        } catch (FileMalformattedException | DataPointMalformattedException | IOException e) {
            e.printStackTrace();
        }
    }
}