package openjade.task.behaviour.ability;

import jade.core.Agent;

import java.util.List;

import openjade.core.behaviours.CyclicTimerBehaviour;
import openjade.task.agent.TaskAgent;
import openjade.task.agent.ontology.Task;
import openjade.task.config.Constants;

import org.apache.log4j.Logger;

public abstract class Ability extends CyclicTimerBehaviour {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(Ability.class);

	protected long capacity = 0;

	private TaskAgent myAgent;

	public abstract long capacity();

	abstract float getCompleted();

	abstract int getPoints(int total);

	abstract String getStatus();

	public Ability(Agent agent, long timer) {
		super(agent, timer);
		myAgent = (TaskAgent) agent;
	}

	public void setAgent(TaskAgent _agent) {
		this.myAgent = _agent;
	}

	public boolean addTask(Task task) {
		capacity += task.getPoints();
		if (capacity > this.capacity()) {
			capacity -= task.getPoints();
			log.debug("addTask [NOK] capacity: " + capacity);
			return false;
		}
		log.debug("addTask [OK] capacity: " + capacity);
		return true;
	}

	@Override
	public void run() {
		List<Task> tasks = myAgent.getTasks().get(Constants.TASK_TO_PROCESS);
		if (!tasks.isEmpty()) {
			Task task = tasks.remove(0);
			task.setCompleted(getCompleted());
			task.setStatus(getStatus());
			task.setPoints(getPoints(task.getPoints()));
			myAgent.getTasks().get(Constants.TASK_TO_COMPLETED).add(task);
			capacity -= task.getPoints();
			log.debug("......... backlog ........ " + myAgent.getTasks().get(Constants.TASK_TO_PROCESS).size());
		}
	}
}
