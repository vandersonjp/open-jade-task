package openjade.task.agent.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TimerAction
* @author ontology bean generator
* @version 2013/01/8, 11:00:44
*/
public class TimerAction implements AgentAction {

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
