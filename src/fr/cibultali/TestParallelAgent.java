package fr.cibultali;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aldric Vitali Silvestre
 */
public class TestParallelAgent extends Agent {

    @Override
    protected void setup() {

        // Wait for compute agents to initiate and properly
        // register to service. Mostly useful in CLI mode
        doWait(1000);

        AgentArguments agentArguments = new AgentArguments(getArguments());
        Function function = agentArguments.createFunction();

        List<AID> computeAgents = searchAgentsFromService();
        // If we have no compute agents, we compute the integral in one pass
        if (computeAgents.isEmpty()) {
            System.err.println("No agent found for parallel computation, process only locally");

            long start = System.nanoTime();
            double resultLocal = function.eval();
            long end = System.nanoTime();

            System.out.println("Integral by TestParallelAgent = " + resultLocal);
            System.out.println("Time for computation: " + computeElapsedTimeMs(start, end) + " milliseconds");
            // Do not want to do something else, stop here
            return;
        }
        // Else, split the function in multiple parts
        System.out.println("Found " + computeAgents.size() + " agents from service COMPUTE");

        // Separate the range in multiple sub-ranges
        Range originalRange = new Range(function.min, function.max);
        List<Range> splits = originalRange.split(computeAgents.size());

        // Do the computation locally first
        System.out.println("==== START LOCAL COMPUTATION ====");
        computeLocally(function, splits);

        // Then we can start the distributed process
        // The time starts here (the sending can cause time losses !)
        System.out.println("==== START DISTRIBUTED COMPUTATION ====");
        long startTime = System.nanoTime();
        sendComputeMessages(agentArguments, computeAgents, splits);

        // Add the behavior to wait for agents to send response
        addBehaviour(new SimpleBehaviour() {

            private double currentSum = 0.0;

            private int resultCountReceived = 0;

            @Override
            public void action() {
                ACLMessage message = receive();
                if (message != null) {
                    resultCountReceived++;
                    double value = parseResponse(message.getContent());
                    currentSum += value;
                } else{
                    block();
                }
            }

            private double parseResponse(String responseContent) {
                // The content is (normally) only a number
                return Double.parseDouble(responseContent);
            }

            @Override
            public boolean done() {
                // Done when all messages are received
                return resultCountReceived >= computeAgents.size();
            }

            @Override
            public int onEnd() {
                // All messages are received, print the result
                long end = System.nanoTime();
                System.out.println("Integral by distributed ComputeAgents = " + currentSum);
                System.out.println("Time for computation: " + computeElapsedTimeMs(startTime, end) + " milliseconds");
                return 0;
            }
        });
    }

    /**
     * Search all agents that have subscribed (and are still subscribed hopefully)
     * to a particular service.
     * @return the agents AIDs.
     * An empty list will be returned if an error occurs
     */
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

    private void computeLocally(Function function, List<Range> splits) {
        long start = System.nanoTime();
        double result = splits.stream()
                .mapToDouble(r -> function.eval(r.getMin(), r.getMax()))
                .sum();
        long end = System.nanoTime();
        System.out.println("Integral by TestParallelAgent = " + result);
        System.out.println("Time for computation: " + computeElapsedTimeMs(start, end) + " milliseconds");
    }

    /**
     * Send a different compute task message to each compute agent
     * @param agentArguments the arguments received by the agent
     * @param computeAgents the AIDs of all compute agents to send a message to
     * @param splits The function bounds to send as input
     */
    private void sendComputeMessages(AgentArguments agentArguments, List<AID> computeAgents, List<Range> splits) {
        // Both lists must have the same size
        if (computeAgents.size() != splits.size()) {
            // If it happens, it is a bug !
            throw new IllegalArgumentException("Not the same number of compute agents and splits found !");
        }
        // We have to iterate both lists at once
        for (int i = 0; i < computeAgents.size(); i++) {
            AID agentAid = computeAgents.get(i);
            Range range = splits.get(i);
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            message.setContent(generateMessageContent(agentArguments, range));
            message.addReceiver(agentAid);
            send(message);
        }
    }

    /**
     * Generate a message content for a {@link ComputeAgent}
     * @param agentArguments the arguments received, contains function name and delta
     * @param split the upper and lower bounds of the function
     * @return the string to send to an agent
     */
    private String generateMessageContent(AgentArguments agentArguments, Range split) {
        // The message is formatted like :
        // [FUNCTION_NAME],[LOWER_BOUND],[UPPER_BOUND],[DELTA]
        return agentArguments.functionName + ","
                + split.getMin() + ","
                + split.getMax() + ","
                + agentArguments.delta;
    }

    /**
     * Compute the elapsed time between two timestamps in nanoseconds
     * @param startTime the start timestamp in nanoseconds
     * @param endTime the end timestamp in nanoseconds
     * @return The difference between both time, converted to milliseconds
     */
    private double computeElapsedTimeMs(long startTime, long endTime) {
        return (endTime - startTime) / 1_000_000.0;
    }

    /**
     * A handler class for converting arguments received by the agent.
     * <p>
     * Also handles the default argument values
     */
    private static class AgentArguments {
        double min = 1.0;
        double max = min + 1;
        double delta = 0.001;
        String functionName = "1/X";

        public AgentArguments(Object[] arguments) {
            parseArguments(arguments);
        }

        /**
         * Create the function from the arguments retrieved
         *
         * @return The function created
         */
        public Function createFunction() {
            return FunctionFactory.createFunction(functionName, min, max, delta);
        }

        private void parseArguments(Object[] arguments) {
            if (arguments == null) {
                return;
            }
            if (arguments.length >= 1) {
                min = parseDoubleOrElse(arguments[0].toString(), 0.0);
            }
            if (arguments.length >= 2) {
                max = parseDoubleOrElse(arguments[1].toString(), min + 1);
            }
            if (arguments.length >= 3) {
                delta = parseDoubleOrElse(arguments[2].toString(), 0.001);
            }
            if (arguments.length >= 4) {
                functionName = arguments[3].toString();
            }
            // Check that max is still greater than min
            if (max < min) {
                System.err.println("Cannot have min lower than max. Set max to " + (min + 1.0));
                max = min + 1.0;
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

    /**
     * A helper class for handling ranges, mainly for splitting it
     * for the function distributed computation.
     * <p>
     * A range object is immutable: each operation will create a new range.
     */
    private static class Range {
        private final double min;
        private final double max;

        public Range(double min, double max) {
            this.min = min;
            this.max = max;
            if (min > max) {
                throw new RuntimeException(String.format("Min (%f) is greater than max (%f)", min, max));
            }
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }

        /**
         * Split a range in multiple parts of the same size
         *
         * @param nbSplits the number of splits to do
         * @return an ordered list containing all splits of the initial range
         */
        public List<Range> split(int nbSplits) {
            double size = max - min;
            double sizeOfSplit = size / nbSplits;
            List<Range> splits = new ArrayList<>(nbSplits);
            // Create the n - 1 first ranges
            for (int splitIndex = 0; splitIndex < nbSplits - 1; splitIndex++) {
                double splitMin = min + (sizeOfSplit * splitIndex);
                double splitMax = splitMin + sizeOfSplit;
                splits.add(new Range(splitMin, splitMax));
            }
            // Create the last range after for avoiding float imprecision.
            // The last range may not have the same size as the other ones
            double lastMin = splits.get(splits.size() - 1).getMax();
            splits.add(new Range(lastMin, max));

            return splits;
        }
    }
}
