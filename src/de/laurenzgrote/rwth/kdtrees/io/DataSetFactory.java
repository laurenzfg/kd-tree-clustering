package de.laurenzgrote.rwth.kdtrees.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import de.laurenzgrote.rwth.kdtrees.data.DataPoint;
import de.laurenzgrote.rwth.kdtrees.data.DataPointMalformattedException;

/**
 * DataSetFactory. Creates a list of data points from a file
*/
public abstract class DataSetFactory {

    /**
     * Reads a dense matrix from a file. Syntax as in CLUTO dense matrix.
     * 1st line: row_count column_count
     * then space seperated vector values
     * 
     * @param path File to read from
     * @return Matrix as specified in File
     * @throws IOException IO Errors are not dealt with within the Reader
     * @throws FileMalformattedException Error if file does not meat specified syntax
     */
    public static List<DataPoint> readFromDenseMatrix(Path path) throws FileMalformattedException, IOException {
        int row_cnt, column_cnt;
        List<String> rows = Files.readAllLines(path); // UTF-8 is assumed
        try {
            String[] params = rows.get(0).split(" ");
            row_cnt = Integer.parseInt(params[0]);
            column_cnt = Integer.parseInt(params[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new FileMalformattedException(path, "First line must be two integers (rows, columns)");
        }
        if (rows.size() < row_cnt + 1) // too many lines are ignored, since there is often an empty line
            throw new FileMalformattedException(path, "First line must contain n+1 lines");
        // ACTUAL READING STARTS HERE
        List<DataPoint> dSet = new ArrayList<>(row_cnt);
        for (int i = 0; i < row_cnt; i++) {
            // We read all the rows
            // let i be the row, then i lays in the i+1th line
            // We know that there are enough lines (checked beforehand)
            String[] line = rows.get(i + 1).split(" ");
            if (line.length != column_cnt)
                throw new FileMalformattedException(path, "Row " + i + " has wrong dimension");
            try {
                double[] vector = new double[line.length];
                for (int j = 0; j < column_cnt; j++) {
                    // throws NFE
                    vector[j] = Double.parseDouble(line[j]);
                }
                DataPoint dPoint = new DataPoint(vector); // throws DPME
                dSet.add(dPoint);
            } catch (NumberFormatException e) {
                throw new FileMalformattedException(path, "Row " + i + " contains non floating number values");
            } catch (DataPointMalformattedException e) {
                throw new FileMalformattedException(path, "Row " + i + ": " + e.getMessage());
            }
        }
        return dSet;
    }
}