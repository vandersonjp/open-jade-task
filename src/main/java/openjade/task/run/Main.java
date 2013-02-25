package openjade.task.run;

import org.apache.log4j.Logger;

public class Main {

	protected static Logger log = Logger.getLogger(Main.class);

	public static void main(String[] args) {
		String[] container = { "-gui", "-host", "127.0.0.1" };
		openjade.Boot.main(container);

//		loadAgents(1,1, "agent_timer", 		"openjade.task.agent.TimerAgent", 		"Freemarket-Container", "123456", null, null);
		
		
		loadAgents( 1, 1, "agent", "openjade.task.agent.TaskAgent", "Agents-Container", "123456", "openjade.trust.DirectModel", "EXCELLENT");
		loadAgents( 2, 2, "agent", "openjade.task.agent.TaskAgent", "Agents-Container", "123456", "openjade.trust.NothingModel", "TERRIBLE");
		loadAgents( 3, 3, "agent", "openjade.task.agent.TaskAgent", "Agents-Container", "123456", "openjade.trust.NothingModel", "EXCELLENT");
//		loadAgents( 5, 8, "agent", "openjade.task.agent.TaskAgent", "Agents-Container", "123456", "openjade.trust.NothingModel", "BAD");
//		loadAgents( 9,12, "agent", "openjade.task.agent.TaskAgent", "Agents-Container", "123456", "openjade.trust.NothingModel", "MODERATE");
//		loadAgents(13,16, "agent", "openjade.task.agent.TaskAgent", "Agents-Container", "123456", "openjade.trust.NothingModel", "GOD");
//		loadAgents(17,20, "agent", "openjade.task.agent.TaskAgent", "Agents-Container", "123456", "openjade.trust.NothingModel", "EXCELLENT");
	}

	private static void loadAgents(int begin, int end, String _nickName, String className, String container, String pass, String _trustModel, String _ability) {
		for (int i = begin; i < end + 1; i++) {
			String nickName = _nickName + "_" + format("000", i);
			String cert = "/certs/" + nickName + ".pfx";
			
			String trustModel = (_trustModel == null ) ? "" : ", " + _trustModel;
			String ability = (_ability == null ) ? "" : ", " + _ability;
			
			
			String[] cont = { "-host", "127.0.0.1", "-container", "-container-name", container, nickName + ":" + className + "(" + cert + ", " + pass + ", " + container + trustModel + ability + ")" };
			openjade.Boot.main(cont);
		}
	}

	private static String format(String _number, int i) {
		String number = _number + i;
		int dif = number.length() - _number.length();
		return number.substring(dif);
	}
}
