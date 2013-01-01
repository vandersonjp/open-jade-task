package openjade.task.agent.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TimerAction
* @author ontology bean generator
* @version 2012/12/27, 17:27:37
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
