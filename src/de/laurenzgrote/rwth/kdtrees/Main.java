package de.laurenzgrote.rwth.kdtrees;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import de.laurenzgrote.rwth.kdtrees.data.DataPoint;
import de.laurenzgrote.rwth.kdtrees.data.DataPointMalformattedException;
import de.laurenzgrote.rwth.kdtrees.data.KDTreeNode;
import de.laurenzgrote.rwth.kdtrees.io.ClusterWriter;
import de.laurenzgrote.rwth.kdtrees.io.DataSetFactory;
import de.laurenzgrote.rwth.kdtrees.io.FileMalformattedException;

public class Main {
    public static void main(String[] args) {
        Path path = Paths.get(args[0]);
        try {
            List<DataPoint> dPoints = DataSetFactory.readFromDenseMatrix(path);
            int minclustersize =  (int) Math.ceil(0.05 * dPoints.size());
            KDTreeNode dSet = new KDTreeNode(dPoints, 0.00001, 0.00006, minclustersize);
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