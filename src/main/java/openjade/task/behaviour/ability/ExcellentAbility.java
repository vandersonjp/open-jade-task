package openjade.task.behaviour.ability;

import jade.core.Agent;
import openjade.task.config.Constants;

public class ExcellentAbility extends Ability {

	public ExcellentAbility(Agent agent) {
		super(agent, 10);
	}

	private static final long serialVersionUID = 1L;

	public long capacity() {
		return 3000;
	}

	@Override
	float getCompleted() {
		return 90 + (int) (Math.random() * 10F);
	}

	@Override
	int getPoints(int total) {
		double base1 = total * 0.9;
		double base2 = total * 0.1;
		return (int) (base1 + (Math.random() * base2));
	}

	@Override
	String getStatus() {
		return Constants.STATUS_DONE;
	}
}
