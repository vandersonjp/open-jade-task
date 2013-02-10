package openjade.task.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.List;

import openjade.core.behaviours.CyclicTimerBehaviour;
import openjade.task.agent.Constants;
import openjade.task.agent.TaskAgent;
import openjade.task.agent.ontology.SendTask;
import openjade.task.agent.ontology.Task;
import openjade.trust.model.Pair;

import org.apache.log4j.Logger;

public class RequestTaskBehaviour extends CyclicTimerBehaviour {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(RequestTaskBehaviour.class);

	private TaskAgent myAgent;

	public RequestTaskBehaviour(Agent agent) {
		super(agent, 100, 100);
		myAgent = (TaskAgent) agent;
	}

	@Override
	public void run() {
		List<Task> tasks = myAgent.getTasks().get(Constants.TASK_TO_DELEGATE);
		try {
			if (!tasks.isEmpty()) {
				AID receive = null;
				
				String[] terms = {"completed", "points"};
				List<Pair> pairs = myAgent.getTrustModel().getPairs(terms);
				
				if (pairs == null || pairs.isEmpty()){
					List<AID>receives = myAgent.getAIDByService(Constants.SERVICE_WORKER);
					receive = receives.get((int) (Math.random() * receives.size()));
				}else{
					receive = pairs.get(pairs.size()-1).getAid();
				}
				
				if (receive != null) {

					Task task = tasks.remove(0);

					SendTask action = new SendTask();
					action.setTask(task);

					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					msg.setSender(myAgent.getAID());
					msg.addReceiver(receive);

					myAgent.signerAndSend(msg, action);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
