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
     * Calculate derivative of the function
     * @return
     */
    public double eval(){
        double derivative = 0;
        for (double i = min; i <= max; i+= delta) {
            derivative += delta * (0.5*(f(i) + f(i+delta)));
        }
        return derivative;
    }

}
