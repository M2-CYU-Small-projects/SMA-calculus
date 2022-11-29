package fr.cibultali;


import jade.core.Agent;

/**
 * @author Aldric Vitali Silvestre
 */
public class TestParallelAgent extends Agent {

    protected double total;

    protected long timestamp;

    @Override
    protected void setup() {
        /**
         * On attend 4 arguments :
         *  - La borne min
         *  - La borne max
         *  - Le delta
         *  - Le nom de la fonction
         *  Si le nom de la fonction existe pas, on prendra une fonction par défaut
         */
        Object[] arguments = getArguments();

        // TODO
    }

    private class AgentArguments {
        double min = 0.0;
        double max = 1.0;
        double delta = 0.01;
        String functionName;

        public AgentArguments(Object[] arguments) {
            if (arguments == null) {
                return;
            }
            if (arguments.length > 1) {
                min = parseDoubleOrElse(arguments[0].toString(), 0.0);
            }
            if (arguments.length > 2) {
                max = parseDoubleOrElse(arguments[1].toString(), 1.0);
            }
        }

        private double parseDoubleOrElse(String value, double defaultValue) {
            try {
                return Double.parseDouble(value);
            } catch (Exception exception) {
                System.err.println("Cannot parse value " + value + ". Use default value: " + defaultValue);
                return defaultValue;
            }
        }
    }
}
