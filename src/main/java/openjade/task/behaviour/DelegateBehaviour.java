package openjade.task.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import openjade.core.OpenAgent;
import openjade.task.agent.ontology.DelegateAction;
import openjade.task.agent.ontology.FreemarketOntology;
import openjade.task.agent.ontology.Task;
import openjade.task.config.Config;

import org.apache.log4j.Logger;

public class DelegateBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	protected static Logger log = Logger.getLogger(DelegateBehaviour.class);

	private List<Task> tasks;
	private OpenAgent myAgent;

	public DelegateBehaviour(Agent agent) {
		super(agent);
		this.tasks = new ArrayList<Task>();
		myAgent = (OpenAgent) agent;
	}

	@Override
	public void action() {
		block(1000);
		if (!tasks.isEmpty()) {
			List<AID> receives = myAgent.getAIDByService(Config.WORKER);
			if (!receives.isEmpty()) {
				Task task = tasks.remove(0);
				int index = (int) (Math.random() * receives.size());
				AID receive = receives.get(index);

				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.setSender(myAgent.getAID());
				DelegateAction sa = new DelegateAction();
				sa.setTask(task);
				long time = GregorianCalendar.getInstance().getTimeInMillis();
				sa.setStartTime(time);
				msg.addReceiver(receive);

				myAgent.fillContent(msg, sa, myAgent.getCodec(), FreemarketOntology.getInstance());
				myAgent.signerAndSend(msg);
			}
		}
	}

	public void addTask(Task task) {
		tasks.add(task);
	}
}
