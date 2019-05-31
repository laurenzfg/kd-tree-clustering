package de.laurenzgrote.rwth.kdtrees;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.laurenzgrote.rwth.kdtrees.data.DataPointMalformatException;
import de.laurenzgrote.rwth.kdtrees.data.DataSet;
import de.laurenzgrote.rwth.kdtrees.data.KDTreeNode;
import de.laurenzgrote.rwth.kdtrees.io.ClusterWriter;
import de.laurenzgrote.rwth.kdtrees.io.DataSetFactory;
import de.laurenzgrote.rwth.kdtrees.io.FileMalformattedException;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        Path path = Paths.get(args[0]);
        try {
            KDTreeNode dSet = new KDTreeNode(DataSetFactory.readFromDenseMatrix(path), 0.001, 0.0001);
            for (int i = 0; i < dSet.getDim(); i++) {
                double avg = dSet.getAvg(i);
                double mean = dSet.getMean(i);
                double variance = dSet.getVariance(i);
                double stddev = dSet.getStddev(i);
                System.out.print("Feature " + i + ": ");
                System.out.println(
                        "Mean: " + mean + " Average: " + avg + " Variance: " + variance + " Stddev: " + stddev);
            }
            ClusterWriter.writeToGnuplot(dSet, Paths.get("out.dat"));
        } catch (FileMalformattedException | IOException | DataPointMalformatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}