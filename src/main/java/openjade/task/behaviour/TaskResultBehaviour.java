package openjade.task.behaviour;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.List;

import openjade.core.behaviours.CyclicTimerBehaviour;
import openjade.task.agent.TaskAgent;
import openjade.task.agent.ontology.DelegateAction;
import openjade.task.agent.ontology.Task;
import openjade.task.config.Constants;

import org.apache.log4j.Logger;

public class TaskResultBehaviour extends CyclicTimerBehaviour {

	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(TaskResultBehaviour.class);

	private TaskAgent myAgent;

	public TaskResultBehaviour(Agent agent) {
		super(agent, 100);
		myAgent = (TaskAgent) agent;
	}

	@Override
	public void run() {
		List<Task> tasks = myAgent.getTasks().get(Constants.TASK_TO_COMPLETED);
		if (!tasks.isEmpty()){
			Task task = tasks.remove(0);
			
			DelegateAction action = new DelegateAction();
			action.setTask(task);
			
			ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
			msg.setSender(myAgent.getAID());
			msg.addReceiver(task.getTaskSender());
			
			myAgent.signerAndSend(msg, action);
			System.out.println("completed");
		}
	}
}
