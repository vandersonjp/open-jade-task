package openjade.task.behaviour.ability;

import jade.core.Agent;
import openjade.task.config.Constants;

public class BadAbility extends Ability {

	public BadAbility(Agent agent) {
		super(agent, 700);
	}

	private static final long serialVersionUID = 1L;

	public long capacity() {
		return 200;
	}
	
	@Override
	float getCompleted() {
		return 30 + (int) (Math.random() * 10F);
	}

	@Override
	int getPoints(int total) {
		double base1 = total * 0.3;
		double base2 = total * 0.15;
		return  (int) (base1 + (Math.random() * base2));
	}

	@Override
	String getStatus() {
		return Constants.STATUS_DONE;
	}
}
