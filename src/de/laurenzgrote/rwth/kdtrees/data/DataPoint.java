package de.laurenzgrote.rwth.kdtrees.data;

/**
 * Datapoint: An vector of double values. Immutable!
 * We assume that data is normalized, thus data is within 0.0 and 1.0
 * This is asserted by the datapoint implementation, an exception ist thrown
 */
public class DataPoint {

    private double[] data;

    /**
     * Constructs a datapoint
     * @param data data vector
     */
    public DataPoint (double[] data) throws DataPointMalformatException {
        this.data = data;
        isSane();
    }

    /**
     * Checks if data is normalized
     */
    private void isSane() throws DataPointMalformatException {
        for (double d : data)
            if (d < 0.0 || d > 1.0)
                throw new DataPointMalformatException("Data point not normalized", this);
    }

    /**
     * @return the data
     */
    public double getData(int feature) {
        return data[feature];
    }
    
    /**
     * @return dimension of the data point
     */
    public int getDim() {
        return data.length;
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder();
        for (double d : data) {
            sBuilder.append(d);
            sBuilder.append(' ');
        }
        return sBuilder.toString();
    }
}