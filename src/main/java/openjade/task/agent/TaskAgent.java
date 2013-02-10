package openjade.task.agent;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import openjade.core.OpenAgent;
import openjade.core.RatingCache;
import openjade.core.annotation.ReceiveMatchMessage;
import openjade.core.behaviours.ReceiveOntologyMessageBehaviour;
import openjade.core.behaviours.RegisterServiceBehaviour;
import openjade.ontology.OpenJadeOntology;
import openjade.ontology.Rating;
import openjade.ontology.SendIteration;
import openjade.ontology.SendRating;
import openjade.task.agent.ontology.SendTask;
import openjade.task.agent.ontology.Task;
import openjade.task.agent.ontology.TaskOntology;
import openjade.task.behaviour.AbilityBehaviour;
import openjade.task.behaviour.AbilityConfig;
import openjade.task.behaviour.RequestTaskBehaviour;
import openjade.task.behaviour.ResponseTaskBehaviour;
import openjade.trust.TrustModelFactory;

import org.apache.log4j.Logger;

public class TaskAgent extends OpenAgent {

	private static final long serialVersionUID = 1L;
	protected static Logger log = Logger.getLogger(TaskAgent.class);
	
	private String keystore;
	private String keystorePassword;
	private RatingCache cache;
	private AbilityBehaviour ability;
	private Hashtable<String, List<Task>> tasks = new Hashtable<String, List<Task>>();

	protected void setup() {
		tasks.put(Constants.TASK_TO_DELEGATE, new ArrayList<Task>());
		tasks.put(Constants.TASK_TO_PROCESS, new ArrayList<Task>());
		tasks.put(Constants.TASK_TO_COMPLETED, new ArrayList<Task>());

		keystore = (String) getArguments()[0];
		keystorePassword = (String) getArguments()[1];
		moveContainer((String) getArguments()[2]);
		if (getArguments().length == 5) {
			trustModel = TrustModelFactory.create((String) getArguments()[3]);
			ability = createAbility((String) getArguments()[4], this);
			addBehaviour(ability);
		}
		log.debug("setup: " + getAID().getLocalName());
		cache = new RatingCache(1, 10);
		addBehaviour(new RegisterServiceBehaviour(this, Constants.SERVICE_WORKER));
		addBehaviour(new ReceiveOntologyMessageBehaviour(this));
		addBehaviour(new RequestTaskBehaviour(this));
		addBehaviour(new ResponseTaskBehaviour(this));
	}

	private AbilityBehaviour createAbility(String _abilityConfig, TaskAgent taskAgent) {
		AbilityConfig ability = AbilityConfig.valueOf(_abilityConfig.toUpperCase());
		return new AbilityBehaviour(taskAgent, ability);
	}

	@ReceiveMatchMessage(action = SendIteration.class, ontology = OpenJadeOntology.class)
	public void receiveTimeAction(ACLMessage message, ContentElement ce) {
		iteration = ((SendIteration) ce).getIteration();
		this.trustModel.setIteration(iteration);
		cache.setIteration(iteration);
		createTasks(5);
		sendSatisfaction();
	}

	@ReceiveMatchMessage(action = SendTask.class, performative = { ACLMessage.REQUEST }, ontology = TaskOntology.class)
	public void receiveTaskRequest(ACLMessage message, ContentElement ce) {
		SendTask action = (SendTask) ce;
		if (ability.addTask(action.getTask())) {
			tasks.get(Constants.TASK_TO_PROCESS).add(action.getTask());
		} else {
			ACLMessage msg = new ACLMessage(ACLMessage.REFUSE);
			msg.setSender(getAID());
			msg.addReceiver(action.getTask().getTaskSender());
			fillContent(msg, action, getCodec(), TaskOntology.getInstance());
			signerAndSend(msg);
		}
	}

	@ReceiveMatchMessage(action = SendTask.class, performative = { ACLMessage.CONFIRM }, ontology = TaskOntology.class)
	public void receiveTaskDone(ACLMessage message, ContentElement ce) {
		SendTask da = (SendTask) ce;
		int satistaction = (da.getTask().getCompleted() + da.getTask().getPoints()) / 2;

		trustModel.addRating(newRating(getAID(), message.getSender(), iteration, "completed", da.getTask().getCompleted()));
		trustModel.addRating(newRating(getAID(), message.getSender(), iteration, "points", da.getTask().getPoints()));
		
		cache.add(newRating(getAID(), message.getSender(), iteration, trustModel.getName(), satistaction));
	}

	@ReceiveMatchMessage(action = SendTask.class, performative = { ACLMessage.REFUSE }, ontology = TaskOntology.class)
	public void receiveTaskRefuse(ACLMessage message, ContentElement ce) {
		log.debug("REFUSE");
		cache.add(newRating(getAID(), message.getSender(), iteration, trustModel.getName(), 0.0F));
		cache.add(newRating(getAID(), message.getSender(), iteration, "Refuse", 1.0F));
	}

	public void sendConfirmTask(SendTask action) {
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.setSender(getAID());
		msg.addReceiver(action.getTask().getTaskSender());
		fillContent(msg, action, getCodec(), TaskOntology.getInstance());
		signerAndSend(msg);
	}

	private void sendSatisfaction() {
		Float value = cache.getValue();
		if (trustModel != null && value != null) {
			List<AID> aids = getAIDByService(OpenAgent.SERVICE_TRUST_MONITOR);
			if (!aids.isEmpty()) {
				SendRating sendRating = new SendRating();

				Rating rating = newRating(getAID(), getAID(), iteration, trustModel.getName(), value);
				sendRating.setRating(rating);

				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setSender(getAID());
				msg.addReceiver(aids.get(0));
				fillContent(msg, sendRating, getCodec(), OpenJadeOntology.getInstance());
				signerAndSend(msg);
			}
		}
		

		value = cache.getValue(iteration, "Refuse");
		if (trustModel != null && value != null) {
			List<AID> aids = getAIDByService(OpenAgent.SERVICE_TRUST_MONITOR);
			if (!aids.isEmpty()) {
				SendRating sendRating = new SendRating();

				Rating rating = newRating(getAID(), getAID(), iteration, "Refuse", value);
				sendRating.setRating(rating);

				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setSender(getAID());
				msg.addReceiver(aids.get(0));
				fillContent(msg, sendRating, getCodec(), OpenJadeOntology.getInstance());
				signerAndSend(msg);
			}
		}

	}

	private void createTasks(int count) {
		for (int i = 0; i < count; i++) {
			Task task = new Task();
			task.setCompleted(0);
			task.setPoints(100);
			task.setStatus(Constants.STATUS_NEW);
			task.setTaskSender(getAID());
			tasks.get(Constants.TASK_TO_DELEGATE).add(task);
			// log.debug("...... createTasks ....... total: " +
			// tasks.get(Constants.TASK_TO_DELEGATE).size() );
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

	public void signerAndSend(ACLMessage _message, AgentAction _action) {
		super.fillContent(_message, _action, getCodec(), TaskOntology.getInstance());
		super.signerAndSend(_message);
	}

	public Hashtable<String, List<Task>> getTasks() {
		return tasks;
	}

	private Rating newRating(AID _client, AID _server, int _iteration, String _term, float _value) {
		Rating rating = new Rating();
		rating.setClient(_client);
		rating.setIteration(_iteration);
		rating.setServer(_server);
		rating.setTerm(_term);
		rating.setValue(_value);
		return rating;
	}

}
