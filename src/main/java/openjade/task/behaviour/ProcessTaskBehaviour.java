package openjade.task.behaviour;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import openjade.core.OpenAgent;
import openjade.task.agent.ontology.DelegateAction;
import openjade.task.agent.ontology.FreemarketOntology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ProcessTaskBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private List<DelegateAction> todo = new ArrayList<DelegateAction>();

	public ProcessTaskBehaviour(Agent agent) {
		super(agent);
	}

	@Override
	public void action() {
		block(1000);
		if (todo != null && !todo.isEmpty()){
			DelegateAction d = todo.remove(0);
			d.getTask().setCompleted(100);
			d.getTask().setStatus("done");
			d.setStartTime(GregorianCalendar.getInstance().getTimeInMillis());
			
			AID taskSender = d.getTask().getTaskSender();
			
			ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
			msg.setSender(myAgent.getAID());
			msg.addReceiver(taskSender);
			OpenAgent agent = (OpenAgent) myAgent;
			agent.fillContent(msg, d, agent.getCodec(), FreemarketOntology.getInstance());
			agent.signerAndSend(msg);
			
			System.out.println("completed");
		}
	}

	public void add(DelegateAction da) {
		todo.add(da);
	}


}
