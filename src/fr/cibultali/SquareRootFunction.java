package fr.cibultali;

import static java.lang.Math.sqrt;

/**
 * Implementation of the square root Function
 *
 * @author Nicolas CIBULKA
 */
public class SquareRootFunction extends Function{


    public SquareRootFunction(double min, double max, double delta){
        super(min, max, delta);
    }

    @Override
    public double f(double x) {
        return sqrt(x);
    }

}
