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

    public static void main(String[] args) {
        Function inverted = new MyFunction(1, 2, 0.0000001);
        System.out.println(inverted.f(2));
        System.out.println(inverted.eval());
    }
}

