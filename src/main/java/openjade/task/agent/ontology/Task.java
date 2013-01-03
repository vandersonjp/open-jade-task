package openjade.task.agent.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Task
* @author ontology bean generator
* @version 2013/01/3, 16:25:09
*/
public class Task implements Concept {

   /**
* Protege name: taskSender
   */
   private AID taskSender;
   public void setTaskSender(AID value) { 
    this.taskSender=value;
   }
   public AID getTaskSender() {
     return this.taskSender;
   }

   /**
* Protege name: completed
   */
   private float completed;
   public void setCompleted(float value) { 
    this.completed=value;
   }
   public float getCompleted() {
     return this.completed;
   }

   /**
* Protege name: taskReceive
   */
   private AID taskReceive;
   public void setTaskReceive(AID value) { 
    this.taskReceive=value;
   }
   public AID getTaskReceive() {
     return this.taskReceive;
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
* Protege name: status
   */
   private String status;
   public void setStatus(String value) { 
    this.status=value;
   }
   public String getStatus() {
     return this.status;
   }

   /**
* Protege name: points
   */
   private int points;
   public void setPoints(int value) { 
    this.points=value;
   }
   public int getPoints() {
     return this.points;
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
