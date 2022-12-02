package fr.cibultali;

import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * A special agent useful for easily creating multiple {@link ComputeAgent}.
 * It is a one task agent that will be inactive after completing its task.
 * <p>
 * The agent can have one numeric argument, indicating the number of agents to create.
 * If no number can be parsed, a default value will be set instead.
 *
 * @author Aldric Vitali Silvestre
 */
public class ComputeCreatorAgent extends Agent {

    private static final int DEFAULT_AGENT_COUNT = 5;

    @Override
    protected void setup() {
        int nbAgents = getAgentCount();
        createAgents(nbAgents);
        // Don't need the agent anymore
        doDelete();
    }

    private int getAgentCount() {
        return Optional.ofNullable(getArguments())
                .filter(args -> args.length > 0)
                .map(args -> args[0].toString())
                .flatMap(this::parseNumber)
                .orElse(DEFAULT_AGENT_COUNT);
    }

    private Optional<Integer> parseNumber(String val) {
        try {
            return Optional.of(Integer.parseInt(val));
        } catch (Exception e) {
            System.err.println("Cannot parse value: \"" + val + "\": " + e.getMessage());
            return Optional.empty();
        }
    }

    private void createAgents(int count) {
        // We don't want to create agents with already existing names
        String generationId = generateRandomString(5);
        System.out.println("Create " + count + " ComputeAgents with generation id: " + generationId);

        AgentContainer container = getContainerController();

        for (int i = 0; i < count; i++) {
            String name = "compute_" + generationId + "_" + (1 + i);
            try {
                AgentController agentController = container.createNewAgent(
                        name,
                        ComputeAgent.class.getCanonicalName(),
                        null
                );
                agentController.start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Generate a random alphanumeric String of wanted length
     *
     * @param length the length of the String to return
     * @return The generated String
     */
    private String generateRandomString(int length) {
        // Original snippet from https://www.baeldung.com/java-random-string#java8-alphanumeric
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'

        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
