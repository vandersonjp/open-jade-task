package openjade.task.behaviour.ability;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import jade.core.Agent;
import openjade.core.OpenJadeException;

@SuppressWarnings("all")
public class AbilityFactory {

	public static Ability create(String clazz, Agent agent) {
		try {
			Class c = Class.forName(clazz);
			Constructor constructor = c.getConstructor(Agent.class);
			return (Ability) constructor.newInstance(agent);
		} catch (ClassNotFoundException e) {
			throw new OpenJadeException(e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new OpenJadeException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new OpenJadeException(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new OpenJadeException(e.getMessage(), e);
		} catch (SecurityException e) {
			throw new OpenJadeException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new OpenJadeException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
	}

}
