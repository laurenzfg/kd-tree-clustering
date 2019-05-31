package de.laurenzgrote.rwth.kdtrees.data;

/**
 * DatapointNotNormalizedException
 */
public class DataPointMalformattedException extends Exception {

    private static final long serialVersionUID = 1L;
    DataPoint faultyDatapoint;

    public DataPointMalformattedException(String clue, DataPoint faultyDatapoint) {
        super(clue);
        this.faultyDatapoint = faultyDatapoint;
    }

    /**
     * @return The faulty datapoint
     */
    public DataPoint getFaultyDatapoint() {
        return faultyDatapoint;
    }
}