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
        Function inverted = new MyFunction(1, 2, 0.25);
        System.out.println(inverted.f(3));
        System.out.println(inverted.eval());
    }
}

