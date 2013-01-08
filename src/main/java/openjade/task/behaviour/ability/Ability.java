package openjade.task.behaviour.ability;

import jade.core.Agent;

import java.util.List;

import openjade.core.behaviours.CyclicTimerBehaviour;
import openjade.task.agent.TaskAgent;
import openjade.task.agent.ontology.Task;
import openjade.task.config.Constants;

import org.apache.log4j.Logger;

public class Ability extends CyclicTimerBehaviour {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(Ability.class);

	protected long capacity = 0;

	private TaskAgent myAgent;

	private AbilityConfig abilityConfig;

	public Ability(Agent agent, AbilityConfig abilityConfig) {
		super(agent, abilityConfig.speed(), abilityConfig.speed());
		myAgent = (TaskAgent) agent;
		this.abilityConfig = abilityConfig;
	}

	public void setAgent(TaskAgent _agent) {
		this.myAgent = _agent;
	}

	int getCompleted() {
		int range = (int) (Math.random() * abilityConfig.completedRange());
		if (Math.random() > 0.50000) {
			return abilityConfig.completed() + range;
		} else {
			return abilityConfig.completed() - range;
		}
	}

	public long capacity() {
		return abilityConfig.capacity();
	}

	int getPoints(int _base) {
		float points = _base * (abilityConfig.points()/100);
		float range = _base *  (abilityConfig.pointsRange()/100);
		if (Math.random() > 0.50000) {
			return (int) (points + range);
		} else {
			return (int) (points - range);
		}
	}

	String getStatus() {
		return Constants.STATUS_DONE;
	}

	public boolean addTask(Task task) {
		capacity += task.getPoints();
		if (capacity > this.capacity()) {
			capacity -= task.getPoints();
			log.debug("addTask [NOK] capacity: " + capacity);
			return false;
		}
		return true;
	}

	@Override
	public void run() {
		List<Task> tasks = myAgent.getTasks().get(Constants.TASK_TO_PROCESS);
		if (!tasks.isEmpty()) {
			Task task = tasks.remove(0);
			capacity -= task.getPoints();
			task.setCompleted(getCompleted());
			task.setStatus(getStatus());
			task.setPoints(getPoints(task.getPoints()));
			myAgent.getTasks().get(Constants.TASK_TO_COMPLETED).add(task);
		}
	}
}
