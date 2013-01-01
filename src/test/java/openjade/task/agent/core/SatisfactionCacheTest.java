package openjade.task.agent.core;

import static org.junit.Assert.*;

import openjade.task.agent.core.SatisfactionCache;
import openjade.task.agent.ontology.SatisfactionAction;

import org.junit.Test;

public class SatisfactionCacheTest {

	@Test
	public void testTimeCache() {
		try {
			new SatisfactionCache(1, 0);
			fail("excetion not throw");
		} catch (Exception e) {
			assertEquals("invalid time cache", e.getMessage());
		}
	}

	@Test
	public void testAddInvalid1() {
		try {
			SatisfactionCache mc = new SatisfactionCache(1, 1);
			mc.add(createSA(2, "a", 2.0F));
			fail("exception don't throwed");
		} catch (Exception e) {
			assertEquals("tempo invalido [2] deve ser menor ou igual a [1]", e.getMessage());
		}
	}

	@Test
	public void testAddInvalid2() {
		try {
			SatisfactionCache mc = new SatisfactionCache(10, 2);
			mc.setCurrentTime(10);
			mc.add(createSA(8, "a", 2.0F));
			fail("exception don't throwed");
		} catch (Exception e) {
			assertEquals("tempo invalido [8] tempo de ver maior que [8]", e.getMessage());
		}
	}

	@Test
	public void testSize_1() {
		try {
			SatisfactionCache mc = new SatisfactionCache(1, 1);
			mc.setCurrentTime(1);

			mc.add(createSA(1, "a", 2.0F));
			mc.add(createSA(1, "a", 2.0F));
			mc.add(createSA(1, "a", 2.0F));

			assertEquals(3, mc.size());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSize_2() {
		try {
			SatisfactionCache mc = new SatisfactionCache(1, 2);
			mc.setCurrentTime(1);

			mc.add(createSA(1, "a", 2.0F));
			mc.add(createSA(1, "a", 2.0F));
			mc.add(createSA(1, "a", 2.0F));

			mc.setCurrentTime(2);

			mc.add(createSA(1, "a", 2.0F));
			mc.add(createSA(2, "a", 2.0F));
			mc.add(createSA(2, "a", 2.0F));

			assertEquals(6, mc.size());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testClean() {
		try {
			SatisfactionCache mc = new SatisfactionCache(5, 3);

			mc.add(createSA(3, "a", 2.0F));
			mc.add(createSA(3, "a", 2.0F));

			mc.add(createSA(4, "a", 2.0F));
			mc.add(createSA(4, "a", 2.0F));

			mc.add(createSA(5, "a", 2.0F));
			mc.add(createSA(5, "a", 2.0F));

			assertEquals(6, mc.size());

			mc.setCurrentTime(6);
			assertEquals(4, mc.size());
			mc.setCurrentTime(7);
			assertEquals(2, mc.size());
			mc.setCurrentTime(8);
			assertEquals(0, mc.size());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCalcException() {
		SatisfactionCache mc = new SatisfactionCache(1, 3);

		mc.add(createSA(1, "a", 2.0F));
		mc.add(createSA(1, "b", 2.0F));

		assertNull(mc.getSatisfaction(5, "a"));
	}

	@Test
	public void testCalcException2() {
		try {
			SatisfactionCache mc = new SatisfactionCache(2, 3);

			mc.add(createSA(1, "a", 10.0F));
			mc.add(createSA(1, "a", 20.0F));
			mc.add(createSA(1, "a", 60.0F));

			assertEquals(mc.getSatisfaction(1, "b"), 0.0F, 0F);
			fail("");
		} catch (Exception e) {
			assertEquals("model [b] not find for time [1]", e.getMessage());
		}
	}

	@Test
	public void testCalc01() {
		try {
			SatisfactionCache mc = new SatisfactionCache(1, 3);
			mc.add(createSA(1, "a", 2.0F));
			assertEquals(mc.getSatisfaction(1, "a"), 2.0F, 0F);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCalc02() {
		try {
			SatisfactionCache mc = new SatisfactionCache(2, 3);

			mc.add(createSA(1, "a", 10.0F));
			mc.add(createSA(1, "a", 20.0F));
			mc.add(createSA(1, "a", 60.0F));

			mc.add(createSA(2, "a", 1.0F));
			mc.add(createSA(2, "b", 2.0F));
			mc.add(createSA(2, "b", 6.0F));

			assertEquals(mc.getSatisfaction(1, "a"), 30.0F, 0F);
			assertEquals(mc.getSatisfaction(2, "a"), 1.0F, 0F);
			assertEquals(mc.getSatisfaction(2, "b"), 4.0F, 0F);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testIsFull() {
		SatisfactionCache mc = new SatisfactionCache(1, 4);
		mc.add(createSA(1, "a", 10.0F));
		assertFalse(mc.isCompleted());

		mc.setCurrentTime(2);
		mc.add(createSA(2, "a", 10.0F));
		assertFalse(mc.isCompleted());

		mc.setCurrentTime(3);
		mc.add(createSA(3, "a", 10.0F));
		assertFalse(mc.isCompleted());

		mc.setCurrentTime(4);
		mc.add(createSA(4, "a", 10.0F));
		assertTrue(mc.isCompleted());
	}

	private SatisfactionAction createSA(int time, String trustmodel, float value) {
		SatisfactionAction sa = new SatisfactionAction();
		sa.setTime(time);
		sa.setTrustmodel(trustmodel);
		sa.setSatisfaction(value);
		return sa;
	}

}
