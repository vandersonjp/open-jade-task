package openjade.task.agent;

import java.io.InputStream;

import openjade.core.OpenAgent;
import openjade.task.behaviour.TimerBehaviour;

import org.apache.log4j.Logger;

public class TimerAgent extends OpenAgent {

	protected static Logger log = Logger.getLogger(TimerAgent.class);

	private static final long serialVersionUID = 1L;

	private String keystore;

	private String keystorePassword;
	
	protected void setup() {
		keystore = (String) getArguments()[0];
		keystorePassword = (String) getArguments()[1];
		moveContainer((String) getArguments()[2]);
		log.debug("setup: " + getAID().getLocalName());
		addBehaviour(new TimerBehaviour(this, 5000));
	}

	@Override
	protected InputStream getKeystore() {
		return TimerAgent.class.getResourceAsStream(keystore);
	}

	@Override
	protected String getKeystorePassword() {
		return keystorePassword;
	}
}
