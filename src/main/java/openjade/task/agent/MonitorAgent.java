package openjade.task.agent;

import jade.content.ContentElement;
import jade.lang.acl.ACLMessage;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;

import openjade.core.OpenAgent;
import openjade.core.annotation.ReceiveMatchMessage;
import openjade.core.behaviours.ReceiveOntologyMessageBehaviour;
import openjade.core.behaviours.RegisterServiceBehaviour;
import openjade.task.agent.core.SatisfactionCache;
import openjade.task.agent.ontology.SatisfactionAction;
import openjade.task.agent.ontology.TaskOntology;
import openjade.task.agent.ontology.TimerAction;
import openjade.task.config.Config;
import openjade.task.gui.MonitorChart;

import org.apache.log4j.Logger;

public class MonitorAgent extends OpenAgent {

	private String keystore;
	private String keystorePassword;
	private static final long serialVersionUID = 1L;
	private SatisfactionCache cache;
	private HashSet<String> models;

	protected static Logger log = Logger.getLogger(MonitorAgent.class);

	protected void setup() {
		super.setup();
		keystore = (String) getArguments()[0];
		keystorePassword = (String) getArguments()[1];
		moveContainer((String) getArguments()[2]);
		log.debug("setup: " + getAID().getLocalName());
		getMonitorChart();
		cache = new SatisfactionCache(1, 3);
		models = new HashSet<String>();
		addBehaviour(new ReceiveOntologyMessageBehaviour(this));
		addBehaviour(new RegisterServiceBehaviour(this, Config.MONITOR));
	}

	@ReceiveMatchMessage(ontology = TaskOntology.class, action = TimerAction.class)
	public void receiveTimeAction(ACLMessage message, ContentElement ce) {
		TimerAction ta = (TimerAction) ce;
		cache.setCurrentTime(ta.getTime());
		if (cache.isCompleted()) {
			Iterator<String> it = models.iterator();
			while (it.hasNext()) {
				String model = it.next();
				Float value = cache.getSatisfaction(cache.getMin(), model);
				if (value != null) {
					getMonitorChart().addValue(model, cache.getMin(), value);
				}
			}
		}
	}

	@ReceiveMatchMessage(ontology = TaskOntology.class, action = SatisfactionAction.class)
	public void receiveTaskDone(ACLMessage message, ContentElement ce) {
		SatisfactionAction sa = (SatisfactionAction) ce;
		cache.add(sa);
		models.add(sa.getTrustmodel());
	}

	@Override
	protected InputStream getKeystore() {
		return TaskAgent.class.getResourceAsStream(keystore);
	}

	@Override
	protected String getKeystorePassword() {
		return keystorePassword;
	}
	
	private MonitorChart getMonitorChart() {
		return MonitorChart.getInstance("Open Jade Monitor", 100D, 180D);
	}
}
