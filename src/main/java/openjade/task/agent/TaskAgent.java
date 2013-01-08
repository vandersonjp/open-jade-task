package openjade.task.agent;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.lang.acl.ACLMessage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import openjade.core.OpenAgent;
import openjade.core.annotation.ReceiveMatchMessage;
import openjade.core.behaviours.ReceiveOntologyMessageBehaviour;
import openjade.core.behaviours.RegisterServiceBehaviour;
import openjade.task.agent.core.SatisfactionCache;
import openjade.task.agent.ontology.DelegateAction;
import openjade.task.agent.ontology.SatisfactionAction;
import openjade.task.agent.ontology.Task;
import openjade.task.agent.ontology.TaskOntology;
import openjade.task.agent.ontology.TimerAction;
import openjade.task.behaviour.TaskDelegateBehaviour;
import openjade.task.behaviour.TaskResultBehaviour;
import openjade.task.behaviour.ability.Ability;
import openjade.task.behaviour.ability.AbilityConfig;
import openjade.task.config.Constants;
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

	private Ability ability;

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
		cache = new SatisfactionCache(1, 10);
		addBehaviour(new RegisterServiceBehaviour(this, Constants.SERVICE_WORKER));
		addBehaviour(new ReceiveOntologyMessageBehaviour(this));
		addBehaviour(new TaskDelegateBehaviour(this));
		addBehaviour(new TaskResultBehaviour(this));
	}

	private Ability createAbility(String _abilityConfig, TaskAgent taskAgent) {
		AbilityConfig ability = AbilityConfig.valueOf(_abilityConfig.toUpperCase());		
		return new Ability(taskAgent, ability);
	}

	@ReceiveMatchMessage(action = TimerAction.class, ontology = TaskOntology.class)
	public void receiveTimeAction(ACLMessage message, ContentElement ce) {
		time = ((TimerAction) ce).getTime();
		cache.setCurrentTime(time);
		createTasks(5);
		sendSatisfaction();
	}

	@ReceiveMatchMessage(action = DelegateAction.class, performative = { ACLMessage.REQUEST }, ontology = TaskOntology.class)
	public void receiveTaskRequest(ACLMessage message, ContentElement ce) {
		DelegateAction action = (DelegateAction) ce;
		if (ability.addTask(action.getTask())) {
			tasks.get(Constants.TASK_TO_PROCESS).add(action.getTask());
		}else{
			ACLMessage msg = new ACLMessage(ACLMessage.REFUSE);
			msg.setSender(getAID());
			msg.addReceiver(action.getTask().getTaskSender());
			fillContent(msg, action, getCodec(), TaskOntology.getInstance());
			signerAndSend(msg);
		}
	}

	@ReceiveMatchMessage(action = DelegateAction.class, performative = { ACLMessage.CONFIRM }, ontology = TaskOntology.class)
	public void receiveTaskDone(ACLMessage message, ContentElement ce) {
		DelegateAction da = (DelegateAction) ce;
		SatisfactionAction sa = new SatisfactionAction();
		sa.setSatisfaction( (da.getTask().getCompleted() + da.getTask().getPoints()) / 2 );
		sa.setTrustmodel(trustModel.getName());
		sa.setTime(time);
		cache.add(sa);
	}

	@ReceiveMatchMessage(action = DelegateAction.class, performative = { ACLMessage.REFUSE }, ontology = TaskOntology.class)
	public void receiveTaskRefuse(ACLMessage message, ContentElement ce) {
		log.debug("REFUSE");
		SatisfactionAction sa = new SatisfactionAction();
		sa.setSatisfaction(0);
		sa.setTrustmodel(trustModel.getName());
		sa.setTime(time);
		cache.add(sa);
	}

	public void sendConfirmTask(DelegateAction da) {
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.setSender(getAID());
		msg.addReceiver(da.getTask().getTaskSender());
		fillContent(msg, da, getCodec(), TaskOntology.getInstance());
		signerAndSend(msg);
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
			msg.addReceiver(getAIDByService(Constants.SERVICE_MONITOR).get(0));
			fillContent(msg, sa, getCodec(), TaskOntology.getInstance());
			signerAndSend(msg);
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
//			log.debug("...... createTasks ....... total: " + tasks.get(Constants.TASK_TO_DELEGATE).size()  );
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

}
