package openjade.task.agent;

import jade.content.ContentElement;
import jade.lang.acl.ACLMessage;

import java.io.InputStream;
import java.util.GregorianCalendar;

import openjade.core.OpenAgent;
import openjade.core.annotation.ReceiveMatchMessage;
import openjade.core.behaviours.ReceiveOntologyMessageBehaviour;
import openjade.core.behaviours.RegisterServiceBehaviour;
import openjade.task.ability.Ability;
import openjade.task.ability.AbilityFactory;
import openjade.task.agent.core.SatisfactionCache;
import openjade.task.agent.ontology.DelegateAction;
import openjade.task.agent.ontology.FreemarketOntology;
import openjade.task.agent.ontology.SatisfactionAction;
import openjade.task.agent.ontology.Task;
import openjade.task.agent.ontology.TimerAction;
import openjade.task.behaviour.DelegateBehaviour;
import openjade.task.config.Config;
import openjade.trust.TrustModel;
import openjade.trust.TrustModelFactory;

import org.apache.log4j.Logger;

public class TaskAgent extends OpenAgent {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(TaskAgent.class);

	protected int time;

	private String keystore;

	private String keystorePassword;

	private TrustModel trustModel;

	private SatisfactionCache cache;

	private DelegateBehaviour delegate;
	
	private Ability ability;

	protected void setup() {
		keystore = (String) getArguments()[0];
		keystorePassword = (String) getArguments()[1];
		moveContainer((String) getArguments()[2]);
		if (getArguments().length == 5) {
			trustModel = TrustModelFactory.create((String) getArguments()[3]);
			ability = AbilityFactory.create((String) getArguments()[4]);
		}
		log.debug("setup: " + getAID().getLocalName());
		cache = new SatisfactionCache(1, 3);
		delegate = new DelegateBehaviour(this);
		addBehaviour(new ReceiveOntologyMessageBehaviour(this));
		addBehaviour(new RegisterServiceBehaviour(this, Config.WORKER));
		addBehaviour(delegate);
	}

	@ReceiveMatchMessage(action = TimerAction.class, ontology = FreemarketOntology.class)
	public void receiveTimeAction(ACLMessage message, ContentElement ce) {
		TimerAction timeAction = (TimerAction) ce;
		setTime(timeAction.getTime());
		sendSatisfaction();
	}

	@ReceiveMatchMessage(action = DelegateAction.class, performative = { ACLMessage.REQUEST }, ontology = FreemarketOntology.class)
	public void receiveTaskRequest(ACLMessage message, ContentElement ce) {
		DelegateAction da = (DelegateAction) ce;
		da.setFinishTime(GregorianCalendar.getInstance().getTimeInMillis());
		
		ability.setTask(da.getTask());
		
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.setSender(getAID());
		msg.addReceiver(da.getTask().getTaskSender());
		fillContent(msg, da, getCodec(), FreemarketOntology.getInstance());
		signerAndSend(msg);
	}

	@ReceiveMatchMessage(action = DelegateAction.class, performative = { ACLMessage.CONFIRM }, ontology = FreemarketOntology.class)
	public void receiveTaskDone(ACLMessage message, ContentElement ce) {
		DelegateAction da = (DelegateAction) ce;
		SatisfactionAction sa = new SatisfactionAction();
		sa.setSatisfaction((da.getTask().getCompleted() + da.getTask().getPoints())/2);
		sa.setTrustmodel(trustModel.getName());
		sa.setTime(time);
		cache.add(sa);
	}

	private void sendSatisfaction() {
		Float value = cache.getSatisfaction();
		if (trustModel != null && value != null) {
			SatisfactionAction sa = new SatisfactionAction();
			sa.setTrustmodel(trustModel.getName());
			sa.setTime(time);
			sa.setSatisfaction(value);

			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setSender(getAID());
			msg.addReceiver(getAIDByService(Config.MONITOR).get(0));
			fillContent(msg, sa, getCodec(), FreemarketOntology.getInstance());
			signerAndSend(msg);
		}
	}

	@Override
	protected InputStream getKeystore() {
		return TaskAgent.class.getResourceAsStream(keystore);
	}

	@Override
	protected String getKeystorePassword() {
		return keystorePassword;
	}

	public void createTask() {
		for (int i = 0; i < 5; i++) {
			Task task = new Task();
			task.setCompleted(0);
			task.setPoints(100);
			task.setStatus("new");
			task.setTaskSender(getAID());
			delegate.addTask(task);
		}
	}

	public void setTime(int _time) {
		this.time = _time;
		cache.setCurrentTime(_time);
		createTask();
	}

}
