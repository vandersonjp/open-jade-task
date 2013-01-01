package openjade.task.ability;

import java.io.Serializable;

import openjade.task.agent.ontology.Task;

public interface Ability extends Serializable{

	public void setTask(Task task);

}
