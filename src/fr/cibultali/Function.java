package fr.cibultali;

/**
 * Abstract representation of a Function
 *
 * @author Nicolas CIBULKA
 */
public abstract class Function {
    // minimum of the interval
    public final double min;

    // maximum of the interval
    public final double max;

    // Precision of the derivative ie distance between two points
    public final double delta;

    /**
     * Constructor of the function
     * @param min
     * @param max
     * @param delta
     */
    public Function(double min, double max, double delta){
        this.min = min;
        this.max = max;
        this.delta = delta;
    }

    /**
     * Evaluate the function on the point X
     * @param x
     * @return
     */
    public abstract double f(double x);

    /**
     * Calculate integral of the function
     * @return the integral of the function between min and max
     */
    public double eval(){
        return eval(min, max);
    }

    /**
     * Calculate derivative of the function in the provided range
     *
     * @param lowerBound the lower bound
     * @param upperBound the upper bound
     * @throws IllegalArgumentException if range provided is outside than the
     * original range of the function
     * @return the integral of the function in the provided range
     */
    public double eval(double lowerBound, double upperBound) {
        if (min > lowerBound || max < upperBound) {
            throw new IllegalArgumentException("Range provided is outside the original function range");
        }
        double derivative = 0;
        for (double i = lowerBound; i <= upperBound; i+= delta) {
            derivative += delta * (0.5*(f(i) + f(i+delta)));
        }
        return derivative;
    }
}
