package openjade.task.behaviour;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import openjade.core.behaviours.BehaviourException;
import openjade.core.behaviours.CyclicTimerBehaviour;
import openjade.task.agent.TimerAgent;
import openjade.task.agent.ontology.TaskOntology;
import openjade.task.agent.ontology.TimerAction;
import openjade.task.config.Constants;

import org.apache.log4j.Logger;

public class TimerBehaviour extends CyclicTimerBehaviour {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(TimerBehaviour.class);

	private TimerAgent myAgent;
	
	private int time = 0;

	public TimerBehaviour(Agent _agent, long sleep) {
		super(_agent, sleep, 0);
		this.myAgent = (TimerAgent) _agent;
	}

	public void run() {
		time++;
		log.debug(".............. time [" + time + "] ..............");
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		String[] services = { Constants.SERVICE_MONITOR, Constants.SERVICE_WORKER };
		try {
			for (String service : services) {
				DFAgentDescription dfd = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType(service);
				dfd.addServices(sd);
				DFAgentDescription[] results = DFService.search(myAgent, dfd);
				for (DFAgentDescription result : results) {
					message.addReceiver(result.getName());
				}
			}
		} catch (FIPAException e) {
			throw new BehaviourException(e.getMessage(), e);
		}
		message.setSender(myAgent.getAID());
		TimerAction action = new TimerAction();
		action.setTime(time);
		myAgent.fillContent(message, action, myAgent.getCodec(), TaskOntology.getInstance());
		myAgent.signerAndSend(message);
	}
}
