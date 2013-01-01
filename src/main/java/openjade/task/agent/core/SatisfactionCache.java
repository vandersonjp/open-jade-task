package openjade.task.agent.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import openjade.task.agent.ontology.SatisfactionAction;

import org.bouncycastle.crypto.RuntimeCryptoException;

public class SatisfactionCache implements Serializable {

	private static final long serialVersionUID = 1L;

	private int cacheTime;
	private int minTime;

	private Hashtable<Integer, List<SatisfactionAction>> cache = new Hashtable<Integer, List<SatisfactionAction>>();

	private int currentTime;

	public SatisfactionCache(int _currentTime, int _timeCache) {
		setTimeCache(_timeCache);
		setCurrentTime(_currentTime);
		minTime = (_currentTime - _timeCache);
		minTime = (minTime <= 1) ? 1 : minTime;
	}

	public void add(SatisfactionAction sa) {
		if (sa.getTime() > (this.currentTime)) {
			throw new RuntimeCryptoException("tempo invalido [" + sa.getTime() + "] deve ser menor ou igual a [" + this.currentTime + "]");
		}
		if (sa.getTime() <= (this.currentTime - this.cacheTime)) {
			throw new RuntimeCryptoException("tempo invalido [" + sa.getTime() + "] tempo de ver maior que [" + (this.currentTime - this.cacheTime) + "]");
		}
		List<SatisfactionAction> list = cache.get(sa.getTime());
		if (list == null) {
			list = new ArrayList<SatisfactionAction>();
			cache.put(sa.getTime(), list);
		}
		list.add(sa);
	}

	public void setCurrentTime(int _currentTime) {
		currentTime = _currentTime;
		clean();
	}

	private void clean() {
		while (minTime <= (currentTime - cacheTime)) {
			cache.remove(minTime);
			minTime++;
		}
	}

	private void setTimeCache(int _timeCache) {
		if (_timeCache <= 0) {
			throw new RuntimeCryptoException("invalid time cache");
		}
		this.cacheTime = _timeCache;
	}

	public int size() {
		int size = 0;
		Enumeration<List<SatisfactionAction>> elements = this.cache.elements();
		while (elements.hasMoreElements()) {
			List<SatisfactionAction> list = (List<SatisfactionAction>) elements.nextElement();
			size += list.size();
		}
		return size;
	}

	public Float getSatisfaction() {
		float count = 0.0F;
		float sum = 0.0F;
		Enumeration<Integer> keys = cache.keys();
		while (keys.hasMoreElements()) {
			Integer key = (Integer) keys.nextElement();
			List<SatisfactionAction> list = cache.get(key);
			for (SatisfactionAction satisfactionAction : list) {
				count++;
				sum += satisfactionAction.getSatisfaction();
			}
			if (count == 0.0F) {
				throw new RuntimeException("cache is null");
			}
			return (sum == 0.0F) ? 0.0F : sum / count;
		}
		return null;
	}

	public Float getSatisfaction(int time, String model) {
		List<SatisfactionAction> list = cache.get(time);
		if (list == null)
			return null;
		float count = 0.0F;
		float sum = 0.0F;
		for (SatisfactionAction satisfactionAction : list) {
			if (satisfactionAction.getTrustmodel().equals(model)) {
				count++;
				sum += satisfactionAction.getSatisfaction();
			}
		}
		if (count == 0.0F) {
			throw new RuntimeException("model [" + model + "] not find for time [" + time + "]");
		}
		return (sum == 0.0F) ? 0.0F : sum / count;
	}

	public boolean isCompleted() {
		return (currentTime - minTime) >= (cacheTime - 1);
	}

	public int getMin() {
		return this.minTime;
	}
}
