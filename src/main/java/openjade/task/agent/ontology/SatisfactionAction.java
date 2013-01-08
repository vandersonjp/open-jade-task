package openjade.task.agent.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: SatisfactionAction
* @author ontology bean generator
* @version 2013/01/8, 11:00:44
*/
public class SatisfactionAction implements AgentAction {

   /**
* Protege name: satisfaction
   */
   private float satisfaction;
   public void setSatisfaction(float value) { 
    this.satisfaction=value;
   }
   public float getSatisfaction() {
     return this.satisfaction;
   }

   /**
* Protege name: trustmodel
   */
   private String trustmodel;
   public void setTrustmodel(String value) { 
    this.trustmodel=value;
   }
   public String getTrustmodel() {
     return this.trustmodel;
   }

   /**
* Protege name: time
   */
   private int time;
   public void setTime(int value) { 
    this.time=value;
   }
   public int getTime() {
     return this.time;
   }

}
