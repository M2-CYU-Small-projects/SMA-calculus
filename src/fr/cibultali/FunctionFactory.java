package fr.cibultali;

/**
 * @author Aldric Vitali Silvestre
 */
public class FunctionFactory {

    public static Function createFunction(String name, double min, double max, double delta) {
        switch (name) {
            case "REVERTED":
                return new MyFunction(min, max, delta);
            default:
                throw new IllegalArgumentException("Function name not found: " + name);
        }
    }
}
