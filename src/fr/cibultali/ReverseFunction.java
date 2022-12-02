package fr.cibultali;

/**
 * Implementation of the reverse Function
 *
 * @author Nicolas CIBULKA
 */
public class ReverseFunction extends Function{

    public ReverseFunction(double min, double max, double delta){
        super(min, max, delta);
    }

    @Override
    public double f(double x) {
        return 1/x;
    }

}

