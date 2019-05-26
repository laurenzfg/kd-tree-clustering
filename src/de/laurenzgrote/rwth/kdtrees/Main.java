package de.laurenzgrote.rwth.kdtrees;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.laurenzgrote.rwth.kdtrees.data.DataSet;
import de.laurenzgrote.rwth.kdtrees.io.DataSetFactory;
import de.laurenzgrote.rwth.kdtrees.io.FileMalformattedException;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        Path path = Paths.get(args[0]);
        DataSet dSet;
        try {
            dSet = DataSetFactory.dataSetFromDenseMatrix(path);
            System.out.println(dSet.avg(2));
        } catch (FileMalformattedException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}