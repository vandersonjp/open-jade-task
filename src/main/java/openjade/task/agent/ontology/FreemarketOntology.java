// file: FreemarketOntology.java generated by ontology bean generator.  DO NOT EDIT, UNLESS YOU ARE REALLY SURE WHAT YOU ARE DOING!
package openjade.task.agent.ontology;

import jade.content.onto.*;
import jade.content.schema.*;
import jade.util.leap.HashMap;
import jade.content.lang.Codec;
import jade.core.CaseInsensitiveString;

/** file: FreemarketOntology.java
 * @author ontology bean generator
 * @version 2012/12/27, 17:27:37
 */
public class FreemarketOntology extends jade.content.onto.Ontology  {
  //NAME
  public static final String ONTOLOGY_NAME = "Freemarket";
  // The singleton instance of this ontology
  private static ReflectiveIntrospector introspect = new ReflectiveIntrospector();
  private static Ontology theInstance = new FreemarketOntology();
  public static Ontology getInstance() {
     return theInstance;
  }


   // VOCABULARY
    public static final String DELEGATEACTION_STARTTIME="startTime";
    public static final String DELEGATEACTION_FINISHTIME="finishTime";
    public static final String DELEGATEACTION_TASK="task";
    public static final String DELEGATEACTION="DelegateAction";
    public static final String SATISFACTIONACTION_SATISFACTION="satisfaction";
    public static final String SATISFACTIONACTION_TRUSTMODEL="trustmodel";
    public static final String SATISFACTIONACTION_TIME="time";
    public static final String SATISFACTIONACTION="SatisfactionAction";
    public static final String TIMERACTION_TIME="time";
    public static final String TIMERACTION="TimerAction";
    public static final String TASK_POINTS="points";
    public static final String TASK_TASKRECEIVE="taskReceive";
    public static final String TASK_COMPLETED="completed";
    public static final String TASK_TASKSENDER="taskSender";
    public static final String TASK_STATUS="status";
    public static final String TASK="Task";

  /**
   * Constructor
  */
  private FreemarketOntology(){ 
    super(ONTOLOGY_NAME, BasicOntology.getInstance());
    try { 

    // adding Concept(s)
    ConceptSchema taskSchema = new ConceptSchema(TASK);
    add(taskSchema, openjade.task.agent.ontology.Task.class);

    // adding AgentAction(s)
    AgentActionSchema timerActionSchema = new AgentActionSchema(TIMERACTION);
    add(timerActionSchema, openjade.task.agent.ontology.TimerAction.class);
    AgentActionSchema satisfactionActionSchema = new AgentActionSchema(SATISFACTIONACTION);
    add(satisfactionActionSchema, openjade.task.agent.ontology.SatisfactionAction.class);
    AgentActionSchema delegateActionSchema = new AgentActionSchema(DELEGATEACTION);
    add(delegateActionSchema, openjade.task.agent.ontology.DelegateAction.class);

    // adding AID(s)

    // adding Predicate(s)


    // adding fields
    taskSchema.add(TASK_STATUS, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    taskSchema.add(TASK_TASKSENDER, (ConceptSchema)getSchema(BasicOntology.AID), ObjectSchema.OPTIONAL);
    taskSchema.add(TASK_COMPLETED, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    taskSchema.add(TASK_TASKRECEIVE, (ConceptSchema)getSchema(BasicOntology.AID), ObjectSchema.OPTIONAL);
    taskSchema.add(TASK_POINTS, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    timerActionSchema.add(TIMERACTION_TIME, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    satisfactionActionSchema.add(SATISFACTIONACTION_TIME, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    satisfactionActionSchema.add(SATISFACTIONACTION_TRUSTMODEL, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    satisfactionActionSchema.add(SATISFACTIONACTION_SATISFACTION, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    delegateActionSchema.add(DELEGATEACTION_TASK, taskSchema, ObjectSchema.OPTIONAL);
    delegateActionSchema.add(DELEGATEACTION_FINISHTIME, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    delegateActionSchema.add(DELEGATEACTION_STARTTIME, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);

    // adding name mappings

    // adding inheritance

   }catch (java.lang.Exception e) {e.printStackTrace();}
  }
  }
