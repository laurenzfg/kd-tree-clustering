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

    // This is a "magic number"
    // I did not make it a parameter bcause 5% proved good
    // and is also proposed in the original paper
    private static double mincluster = 0.05;
    public static void main(String[] args) {
        Path path = Paths.get(args[0]);
        double tresh_maxdiff = Double.parseDouble(args[1]);
        double tresh_variance = Double.parseDouble(args[2]);
        try {
            List<DataPoint> dPoints = DataSetFactory.readFromDenseMatrix(path);
            int minclustersize =  (int) Math.ceil(mincluster * dPoints.size());
            TreeNode dSet = new KDTreeNode(dPoints, tresh_maxdiff, tresh_variance, minclustersize);
            ClusterWriter.writeToGnuplot(dSet, Paths.get("out.dat"));
        } catch (FileMalformattedException | DataPointMalformattedException | IOException e) {
            e.printStackTrace();
        }
    }
}