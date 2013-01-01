package openjade.task.behaviour;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import openjade.core.behaviours.BehaviourException;
import openjade.task.agent.TimerAgent;
import openjade.task.agent.ontology.FreemarketOntology;
import openjade.task.agent.ontology.TimerAction;
import openjade.task.config.Config;

import org.apache.log4j.Logger;

public class TimerBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(TimerBehaviour.class);

	private long sleep;

	private TimerAgent myAgent;
	

	public TimerBehaviour(Agent a, long sleep) {
		super(a);
		this.myAgent = (TimerAgent) a;
		this.sleep = sleep;
	}

	public void action() {
		long t0 = System.currentTimeMillis();
		myAgent.addClick();
		log.debug("click: " + myAgent.getTime());
		ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
		String[] services = { Config.MONITOR, Config.WORKER };
		try {
			for (String service : services) {
				DFAgentDescription dfd = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType(service);
				dfd.addServices(sd);
				DFAgentDescription[] results = DFService.search(myAgent, dfd);
				for (DFAgentDescription result : results) {
					reply.addReceiver(result.getName());
				}
			}
		} catch (FIPAException e) {
			throw new BehaviourException(e.getMessage(), e);
		}
		reply.setSender(myAgent.getAID());
		TimerAction change = new TimerAction();
		change.setTime(myAgent.getTime());
		myAgent.fillContent(reply, change, myAgent.getCodec(), FreemarketOntology.getInstance());
		myAgent.signerAndSend(reply);
		try {
			long dt = sleep - (System.currentTimeMillis() - t0);
			if (dt > 0) {
				Thread.sleep(dt);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
