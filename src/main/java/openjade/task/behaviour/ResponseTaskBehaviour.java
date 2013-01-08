package openjade.task.behaviour;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.List;

import openjade.core.behaviours.CyclicTimerBehaviour;
import openjade.task.agent.Constants;
import openjade.task.agent.TaskAgent;
import openjade.task.agent.ontology.SendTask;
import openjade.task.agent.ontology.Task;

import org.apache.log4j.Logger;

public class ResponseTaskBehaviour extends CyclicTimerBehaviour {

	private static final long serialVersionUID = 1L;
	
	protected static Logger log = Logger.getLogger(ResponseTaskBehaviour.class);

	private TaskAgent myAgent;

	public ResponseTaskBehaviour(Agent agent) {
		super(agent, 100);
		myAgent = (TaskAgent) agent;
	}

	@Override
	public void run() {
		List<Task> tasks = myAgent.getTasks().get(Constants.TASK_TO_COMPLETED);
		if (!tasks.isEmpty()){
			Task task = tasks.remove(0);
			
			SendTask action = new SendTask();
			action.setTask(task);
			
			ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
			msg.setSender(myAgent.getAID());
			msg.addReceiver(task.getTaskSender());
			
			myAgent.signerAndSend(msg, action);
		}
	}
}
