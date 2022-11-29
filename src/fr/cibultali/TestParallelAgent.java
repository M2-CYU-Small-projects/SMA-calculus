package fr.cibultali;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
         */
        AgentArguments agentArguments = new AgentArguments(getArguments());
        Function function = agentArguments.createFunction();

        List<AID> computeAgents = searchAgentsFromService();
        if (computeAgents.isEmpty()) {
            System.err.println("No agent found for computation");
        }
        System.out.println("Found " + computeAgents.size() + " agents from service COMPUTE");

        long start = System.nanoTime();
        double resultLocal = function.eval();
        long end = System.nanoTime();
        System.out.println("Integral by TestParallelAgent = " + resultLocal);
        System.out.println("Time for computation: " + ((end - start) / 1_000_000.0) + " milliseconds");

        // TODO separate function with different ranges
        // do the sum locally
        // THEN, send a message to all compute agents,
        // THEN wait for answer and compute sum

        addBehaviour(new SimpleBehaviour() {
            @Override
            public void action() {

            }

            @Override
            public boolean done() {
                // Done when all messages are received
                return false;
            }
        });
    }

    private List<AID> searchAgentsFromService() {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("COMPUTE");
        template.addServices(serviceDescription);
        try {
            DFAgentDescription[] results = DFService.search(this, template);
            return Arrays.stream(results)
                    .map(DFAgentDescription::getName)
                    .collect(Collectors.toList());
        } catch (FIPAException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private class AgentArguments {
        double min = 0.0;
        double max = min + 1;
        double delta = 0.001;
        String functionName = "1/X";

        public AgentArguments(Object[] arguments) {
            parseArguments(arguments);
        }

        public Function createFunction() {
            return FunctionFactory.createFunction(functionName, min, max, delta);
        }

        private void parseArguments(Object[] arguments) {
            if (arguments == null) {
                return;
            }
            if (arguments.length > 1) {
                min = parseDoubleOrElse(arguments[0].toString(), 0.0);
            }
            if (arguments.length > 2) {
                max = parseDoubleOrElse(arguments[1].toString(), min + 1);
                if (max < min) {
                    System.err.println("Cannot have min lower than max. Set max to " + (min + 1.0));
                    max = min + 1.0;
                }
            }
            if (arguments.length > 3) {
                delta = parseDoubleOrElse(arguments[2].toString(), 0.001);
            }
            if (arguments.length > 4) {
                functionName = arguments[3].toString();
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
