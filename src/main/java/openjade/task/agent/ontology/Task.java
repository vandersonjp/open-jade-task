package openjade.task.agent.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Task
* @author ontology bean generator
* @version 2012/12/27, 17:27:37
*/
public class Task implements Concept {

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
* Protege name: points
   */
   private int points;
   public void setPoints(int value) { 
    this.points=value;
   }
   public int getPoints() {
     return this.points;
   }

}
