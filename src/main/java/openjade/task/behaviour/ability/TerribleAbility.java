package openjade.task.behaviour.ability;

import jade.core.Agent;
import openjade.task.config.Config;

public class TerribleAbility extends Ability {

	private static final long serialVersionUID = 1L;

	public TerribleAbility(Agent agent) {
		super(agent);
	}

	public long capacity() {
		return 1000;
	}

	public long speed() {
		return 5000;
	}

	@Override
	float getCompleted() {
		return 100F;
		// return 10 + (int) (Math.random() * 10F);

	}

	@Override
	int getPoints(int total) {
		return 100;
		// double base1 = total * 0.1;
		// double base2 = total * 0.1;
		// return (int) (base1 + (Math.random() * base2));
	}

	@Override
	String getStatus() {
		return Config.STATUS_DONE;
	}

}
