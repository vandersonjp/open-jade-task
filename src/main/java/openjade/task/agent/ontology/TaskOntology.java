// file: TaskOntology.java generated by ontology bean generator.  DO NOT EDIT, UNLESS YOU ARE REALLY SURE WHAT YOU ARE DOING!
package openjade.task.agent.ontology;

import jade.content.onto.*;
import jade.content.schema.*;
import jade.util.leap.HashMap;
import jade.content.lang.Codec;
import jade.core.CaseInsensitiveString;

/** file: TaskOntology.java
 * @author ontology bean generator
 * @version 2013/01/8, 14:07:34
 */
public class TaskOntology extends jade.content.onto.Ontology  {
  //NAME
  public static final String ONTOLOGY_NAME = "Task";
  // The singleton instance of this ontology
  private static ReflectiveIntrospector introspect = new ReflectiveIntrospector();
  private static Ontology theInstance = new TaskOntology();
  public static Ontology getInstance() {
     return theInstance;
  }


   // VOCABULARY
    public static final String SENDTASK_TASK="task";
    public static final String SENDTASK="SendTask";
    public static final String TASK_COMPLETED="completed";
    public static final String TASK_STATUS="status";
    public static final String TASK_POINTS="points";
    public static final String TASK_TASKRECEIVE="taskReceive";
    public static final String TASK_TASKSENDER="taskSender";
    public static final String TASK="Task";

  /**
   * Constructor
  */
  private TaskOntology(){ 
    super(ONTOLOGY_NAME, BasicOntology.getInstance());
    try { 

    // adding Concept(s)
    ConceptSchema taskSchema = new ConceptSchema(TASK);
    add(taskSchema, openjade.task.agent.ontology.Task.class);

    // adding AgentAction(s)
    AgentActionSchema sendTaskSchema = new AgentActionSchema(SENDTASK);
    add(sendTaskSchema, openjade.task.agent.ontology.SendTask.class);

    // adding AID(s)

    // adding Predicate(s)


    // adding fields
    taskSchema.add(TASK_TASKSENDER, (ConceptSchema)getSchema(BasicOntology.AID), ObjectSchema.OPTIONAL);
    taskSchema.add(TASK_TASKRECEIVE, (ConceptSchema)getSchema(BasicOntology.AID), ObjectSchema.OPTIONAL);
    taskSchema.add(TASK_POINTS, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    taskSchema.add(TASK_STATUS, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    taskSchema.add(TASK_COMPLETED, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    sendTaskSchema.add(SENDTASK_TASK, taskSchema, ObjectSchema.OPTIONAL);

    // adding name mappings

    // adding inheritance

   }catch (java.lang.Exception e) {e.printStackTrace();}
  }
  }
