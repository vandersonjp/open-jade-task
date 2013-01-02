package openjade.task.ability;

import openjade.task.agent.ontology.Task;

public class TerribleAbility implements Ability {

	private static final long serialVersionUID = 1L;

	public void setTask(Task task) {
		task.setCompleted(10 + (int) (Math.random() * 10F));
		task.setStatus("DONE");
		task.setPoints(10 + (int) (Math.random() * 10F));
	}

}
