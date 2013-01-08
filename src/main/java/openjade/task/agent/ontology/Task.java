package openjade.task.agent.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Task
* @author ontology bean generator
* @version 2013/01/8, 11:00:44
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
   private int completed;
   public void setCompleted(int value) { 
    this.completed=value;
   }
   public int getCompleted() {
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

}
