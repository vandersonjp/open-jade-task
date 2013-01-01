package openjade.task.ability;

import openjade.task.agent.ontology.Task;

public class GodAbility implements Ability {

	private static final long serialVersionUID = 1L;

	public void setTask(Task task) {
		task.setCompleted(50 + (int) (Math.random() * 10F));
		task.setStatus("DONE");
		task.setPoints(50 + (int) (Math.random() * 10F));
	}

}
