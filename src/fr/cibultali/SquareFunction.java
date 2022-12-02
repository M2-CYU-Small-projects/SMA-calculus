package fr.cibultali;

/**
 * Implementation of the square Function
 *
 * @author Nicolas CIBULKA
 */
public class SquareFunction extends Function{

    public SquareFunction(double min, double max, double delta){
        super(min, max, delta);
    }

    @Override
    public double f(double x) {
        return x*x;
    }

}
