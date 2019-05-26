package de.laurenzgrote.rwth.kdtrees.data;

/**
 * DatapointNotNormalizedException
 */
public class DataPointMalformatException extends Exception {

    DataPoint faultyDatapoint;

    public DataPointMalformatException(String clue, DataPoint faultyDatapoint) {
        super(clue);
        this.faultyDatapoint = faultyDatapoint;
    }

    /**
     * @return the faultyDatapoint
     */
    public DataPoint getFaultyDatapoint() {
        return faultyDatapoint;
    }
}