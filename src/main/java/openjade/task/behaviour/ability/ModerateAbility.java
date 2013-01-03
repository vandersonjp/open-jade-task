package openjade.task.behaviour.ability;

import jade.core.Agent;
import openjade.task.config.Config;

public class ModerateAbility extends Ability {

	private static final long serialVersionUID = 1L;

	public ModerateAbility(Agent agent) {
		super(agent);
	}

	public long capacity() {
		return 300;
	}

	public long speed() {
		return 500;
	}

	@Override
	float getCompleted() {
		return 50 + (int) (Math.random() * 10F);
	}

	@Override
	int getPoints(int total) {
		double base1 = total * 0.5;
		double base2 = total * 0.1;
		return (int) (base1 + (Math.random() * base2));
	}

	@Override
	String getStatus() {
		return Config.STATUS_DONE;
	}

}
