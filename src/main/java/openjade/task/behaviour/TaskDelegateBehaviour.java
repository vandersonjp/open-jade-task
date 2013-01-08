package openjade.task.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.List;

import openjade.core.behaviours.CyclicTimerBehaviour;
import openjade.task.agent.TaskAgent;
import openjade.task.agent.ontology.DelegateAction;
import openjade.task.agent.ontology.Task;
import openjade.task.config.Constants;

import org.apache.log4j.Logger;

public class TaskDelegateBehaviour extends CyclicTimerBehaviour {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(TaskDelegateBehaviour.class);

	private TaskAgent myAgent;

	public TaskDelegateBehaviour(Agent agent) {
		super(agent, 100, 100);
		myAgent = (TaskAgent) agent;
	}

	@Override
	public void run() {
		List<Task> tasks = myAgent.getTasks().get(Constants.TASK_TO_DELEGATE);
		try {
			if (!tasks.isEmpty()) {
				List<AID> receives = myAgent.getAIDByService(Constants.SERVICE_WORKER);
				if (!receives.isEmpty()) {

					Task task = tasks.remove(0);
					AID receive = receives.get((int) (Math.random() * receives.size()));

					DelegateAction action = new DelegateAction();
					action.setTask(task);

					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					msg.setSender(myAgent.getAID());
					msg.addReceiver(receive);

					myAgent.signerAndSend(msg, action);
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
	}
}
