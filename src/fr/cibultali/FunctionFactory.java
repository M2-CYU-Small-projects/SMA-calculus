package fr.cibultali;

/**
 * The factory used by {@link ComputeAgent} and {@link TestParallelAgent} in order to create function instances
 * from the arguments received.
 *
 * @author Aldric Vitali Silvestre
 */
public class FunctionFactory {

    /**
     * Create the wanted function from all the parameters provided
     * @param name
     * @param min
     * @param max
     * @param delta
     * @throws IllegalArgumentException if the function name is not recognized
     * @return the created function
     */
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
