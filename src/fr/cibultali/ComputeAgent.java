package fr.cibultali;


import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 * @author Aldric Vitali Silvestre
 */
public class ComputeAgent extends Agent {

    private static final String SERVICE_TYPE = "COMPUTE";

    protected Function function;

    @Override
    protected void setup() {
        registerAsService();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage message = receive();
                if (message != null) {
                    AgentArguments agentArguments = convertArgsFromMessage(message.getContent());
                    function = createFunction(agentArguments);
                    double result = function.eval();
                    sendResponse(message, result);
                    System.out.println(String.format(
                            "Agent %s send response for function between %,.2f and %,.2f : %,.2f",
                            getLocalName(),
                            agentArguments.min,
                            agentArguments.max,
                            result
                    ));
                }
                block();
            }
        });
    }

    /**
     * Parse the arguments in the received message's content
     *
     * @param messageContent The received message's content
     * @return the arguments retrieved
     */
    private AgentArguments convertArgsFromMessage(String messageContent) {
        // The message is formatted like :
        // [FUNCTION_NAME],[LOWER_BOUND],[UPPER_BOUND],[DELTA]
        String[] split = messageContent.split(",");
        return new AgentArguments(
                split[0],
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3])
        );
    }

    /**
     * Create the function from the arguments, using the {@link FunctionFactory}
     *
     * @param agentArguments the arguments for creating the function
     * @return the wanted function
     */
    private Function createFunction(AgentArguments agentArguments) {
        return FunctionFactory.createFunction(
                agentArguments.functionName,
                agentArguments.min,
                agentArguments.max,
                agentArguments.delta
        );
    }

    /**
     * Send the response to the expeditor of the message
     *
     * @param message the original message to reply to
     * @param value   the value to return
     */
    private void sendResponse(ACLMessage message, double value) {
        ACLMessage reply = message.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        reply.setContent(String.valueOf(value));
        send(reply);
    }

    @Override
    protected void takeDown() {
        // Deregister from the yellow pages
        System.out.println(String.format("Deregister \"%s\" as service of type \"%s\"", getLocalName(), SERVICE_TYPE));
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    private void registerAsService() {
        System.out.println(String.format("Register \"%s\" as service of type \"%s\"", getLocalName(), SERVICE_TYPE));
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(SERVICE_TYPE);
        serviceDescription.setName(getLocalName());
        register(serviceDescription);
    }

    private void register(ServiceDescription serviceDescription) {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        dfAgentDescription.addServices(serviceDescription);

        try {
            DFService.register(this, dfAgentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the arguments of the agent.
     * <p>
     * This agent does not have any default argument.
     */
    private class AgentArguments {
        String functionName;
        double min;
        double max;
        double delta;

        public AgentArguments(String functionName, double min, double max, double delta) {
            this.functionName = functionName;
            this.min = min;
            this.max = max;
            this.delta = delta;
        }
    }
}
