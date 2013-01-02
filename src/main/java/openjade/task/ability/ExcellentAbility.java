package openjade.task.ability;

import openjade.task.agent.ontology.Task;

public class ExcellentAbility implements Ability {

	private static final long serialVersionUID = 1L;

	public void setTask(Task task) {
		task.setCompleted(90 + (int) (Math.random() * 10F));
		task.setStatus("DONE");
		task.setPoints(90 + (int) (Math.random() * 10F));
	}

}
