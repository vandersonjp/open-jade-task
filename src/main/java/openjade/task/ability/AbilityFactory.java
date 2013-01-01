package openjade.task.ability;

import openjade.core.OpenJadeException;

@SuppressWarnings("rawtypes")
public class AbilityFactory {

	public static Ability create(String clazz) {
		try {
			Class c = Class.forName(clazz);
			return (Ability) c.newInstance();
		} catch (ClassNotFoundException e) {
			throw new OpenJadeException(e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new OpenJadeException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
	}

}
