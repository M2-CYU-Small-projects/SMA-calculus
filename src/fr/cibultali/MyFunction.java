package fr.cibultali;

/**
 * @author Nicolas CIBULKA
 */
public class MyFunction extends Function{

    public MyFunction(double min, double max, double delta){
        super(min, max, delta);
    }

    @Override
    public double f(double x) {
        return 1/x;
    }
}
