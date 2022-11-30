package fr.cibultali;


import jade.core.Agent;
import jade.core.ServiceDescriptor;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * @author Aldric Vitali Silvestre
 */
public class ComputeAgent extends Agent {

    private static final String SERVICE_TYPE = "COMPUTE";

    protected Function function;

    @Override
    protected void setup() {
        registerAsService();

        // TODO add behavior for receiving the message etc.
    }

    @Override
    protected void takeDown() {
        // Deregister from the yellow pages
        System.out.println(String.format("Deregister \"%s\" as service of type \"%s\"", getLocalName(), SERVICE_TYPE));
        try{
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
}
