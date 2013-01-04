package openjade.task.behaviour.ability;

import jade.core.Agent;
import openjade.task.config.Config;
import openjade.task.config.Constants;

public class GodAbility extends Ability {

	private static final long serialVersionUID = 1L;

	public GodAbility(Agent agent) {
		super(agent);
	}

	public long capacity() {
		return 400;
	}

	public long speed() {
		return 400;
	}

	@Override
	float getCompleted() {
		return 70 + (int) (Math.random() * 10F);
	}

	@Override
	int getPoints(int total) {
		double base1 = total * 0.7;
		double base2 = total * 0.1;
		return (int) (base1 + (Math.random() * base2));
	}

	@Override
	String getStatus() {
		return Constants.STATUS_DONE;
	}
}
