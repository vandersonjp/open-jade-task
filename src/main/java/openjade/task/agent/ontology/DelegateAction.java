package openjade.task.agent.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: DelegateAction
* @author ontology bean generator
* @version 2012/12/27, 17:27:37
*/
public class DelegateAction implements AgentAction {

   /**
* Protege name: task
   */
   private Task task;
   public void setTask(Task value) { 
    this.task=value;
   }
   public Task getTask() {
     return this.task;
   }

   /**
* Protege name: finishTime
   */
   private float finishTime;
   public void setFinishTime(float value) { 
    this.finishTime=value;
   }
   public float getFinishTime() {
     return this.finishTime;
   }

   /**
* Protege name: startTime
   */
   private float startTime;
   public void setStartTime(float value) { 
    this.startTime=value;
   }
   public float getStartTime() {
     return this.startTime;
   }

}
