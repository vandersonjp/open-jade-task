package openjade.task.behaviour.ability;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

import java.util.List;

import openjade.task.agent.TaskAgent;
import openjade.task.agent.ontology.Task;
import openjade.task.config.Config;

import org.apache.log4j.Logger;

public abstract class Ability extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(Ability.class);

	protected long capacity = 0;

	private TaskAgent myAgent;

	public abstract long capacity();

	public abstract long speed();

	abstract float getCompleted();

	abstract int getPoints(int total);

	abstract String getStatus();

	public Ability(Agent agent) {
		super(agent);
		myAgent = (TaskAgent) agent;
	}

	public void setAgent(TaskAgent _agent) {
		this.myAgent = _agent;
	}

	public boolean addTask(Task task) {
		capacity += task.getPoints();
		if (capacity > this.capacity()) {
			capacity -= task.getPoints();
			log.debug("Nok capacity: " + capacity);
			return false;
		}
		log.debug("ok capacity: " + capacity);
		return true;
	}

	@Override
	public void action() {
		List<Task> tasks = myAgent.getTasks().get(Config.TASK_TO_PROCESS);
		block(speed());
		if (!tasks.isEmpty()) {
			log.debug(myAgent.getAID().getLocalName() + " capacity:" + capacity);
			Task task = tasks.remove(0);
			task.setCompleted(getCompleted());
			task.setStatus(getStatus());
			task.setPoints(getPoints(task.getPoints()));
			myAgent.getTasks().get(Config.TASK_TO_COMPLETED).add(task);
			capacity -= task.getPoints();
		} else {
			log.debug("empty: " + myAgent.getAID().getLocalName() + " capacity: " + capacity);
		}
	}
}
