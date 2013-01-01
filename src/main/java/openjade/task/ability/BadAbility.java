package openjade.task.ability;

import openjade.task.agent.ontology.Task;

public class BadAbility implements Ability {

	private static final long serialVersionUID = 1L;

	public void setTask(Task task) {
		task.setCompleted(30 + (int) (Math.random() * 15F));
		task.setStatus("DONE");
		task.setPoints(30 + (int) (Math.random() * 15F));
	}

}
