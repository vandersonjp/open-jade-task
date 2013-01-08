package openjade.task.agent;

import jade.content.ContentElement;
import jade.lang.acl.ACLMessage;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;

import openjade.core.OpenAgent;
import openjade.core.RatingCache;
import openjade.core.annotation.ReceiveMatchMessage;
import openjade.core.behaviours.ReceiveOntologyMessageBehaviour;
import openjade.core.behaviours.RegisterServiceBehaviour;
import openjade.ontology.OpenJadeOntology;
import openjade.ontology.SendIteration;
import openjade.ontology.SendRating;
import openjade.task.config.Constants;
import openjade.task.gui.MonitorChart;

import org.apache.log4j.Logger;

public class MonitorAgent extends OpenAgent {

	private String keystore;
	private String keystorePassword;
	private static final long serialVersionUID = 1L;
	private RatingCache cache;
	private HashSet<String> models;

	protected static Logger log = Logger.getLogger(MonitorAgent.class);

	protected void setup() {
		super.setup();
		keystore = (String) getArguments()[0];
		keystorePassword = (String) getArguments()[1];
		moveContainer((String) getArguments()[2]);
		log.debug("setup: " + getAID().getLocalName());
		getMonitorChart();
		cache = new RatingCache(1, 3);
		models = new HashSet<String>();
		addBehaviour(new ReceiveOntologyMessageBehaviour(this));
		addBehaviour(new RegisterServiceBehaviour(this, Constants.SERVICE_MONITOR));
	}

	@ReceiveMatchMessage(ontology = OpenJadeOntology.class, action = SendIteration.class)
	public void receiveTimeAction(ACLMessage message, ContentElement ce) {
		SendIteration ta = (SendIteration) ce;
		cache.setIteration(ta.getIteration());
		if (cache.isCompleted()) {
			Iterator<String> it = models.iterator();
			while (it.hasNext()) {
				String model = it.next();
				Float value = cache.getValue(cache.getMin(), model);
				if (value != null) {
					getMonitorChart().addValue(model, cache.getMin(), value);
				}
			}
		}
	}

	@ReceiveMatchMessage(ontology = OpenJadeOntology.class, action = SendRating.class)
	public void receiveTaskDone(ACLMessage message, ContentElement ce) {
		SendRating sa = (SendRating) ce;
		cache.add(sa.getRating());
		models.add(sa.getRating().getTerm());
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
