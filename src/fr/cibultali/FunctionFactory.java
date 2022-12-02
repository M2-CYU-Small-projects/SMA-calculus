package fr.cibultali;

/**
 * @author Aldric Vitali Silvestre
 */
public class FunctionFactory {

    public static Function createFunction(String name, double min, double max, double delta) {
        switch (name) {
            case "1/X":
                return new ReverseFunction(min, max, delta);
            case "X^2":
                return new SquareFunction(min, max, delta);
            case "X^1/2":
                return new SquareRootFunction(min, max, delta);
            default:
                throw new IllegalArgumentException("Function name not found: " + name);
        }
    }
}
