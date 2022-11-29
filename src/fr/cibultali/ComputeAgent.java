package fr.cibultali;


import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * @author Aldric Vitali Silvestre
 */
public class ComputeAgent extends Agent {

    protected Function function;

    public ComputeAgent(Function function) {
        this.function = function;
    }

    @Override
    protected void setup() {

        /**
         * On va se réserver un service
         */

        super.setup();
    }

    @Override
    protected void takeDown() {
        super.takeDown();
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
