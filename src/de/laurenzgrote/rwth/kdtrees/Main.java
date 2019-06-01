package de.laurenzgrote.rwth.kdtrees;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import de.laurenzgrote.rwth.kdtrees.data.DataPoint;
import de.laurenzgrote.rwth.kdtrees.data.DataPointMalformattedException;
import de.laurenzgrote.rwth.kdtrees.data.SortedKDTreeNode;
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
            SortedKDTreeNode dSet = new SortedKDTreeNode(dPoints, tresh_maxdiff, tresh_variance, minclustersize);
            for (int i = 0; i < dSet.getDim(); i++) {
                double avg = dSet.getAvg(i);
                double mean = dSet.getMean(i);
                double variance = dSet.getVariance(i);
                double stddev = dSet.getStddev(i);
                System.out.print("Feature " + i + ": ");
                System.out.println("Mean: " + mean + " Average: " + avg + 
                    " Variance: " + variance + " Stddev: " + stddev);
            }
            ClusterWriter.writeToGnuplot(dSet, Paths.get("out.dat"));
        } catch (FileMalformattedException | DataPointMalformattedException | IOException e) {
            e.printStackTrace();
        }
    }
}