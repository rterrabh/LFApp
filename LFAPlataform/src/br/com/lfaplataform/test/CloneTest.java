package br.com.lfaplataform.test;

import junit.framework.TestCase;

import org.junit.Test;

import br.com.lfaplataform.vo.Grammar;
import br.com.lfaplataform.vo.Rule;

public class CloneTest extends TestCase {

	private Grammar g;

	@Override
	protected void setUp() throws Exception {
		String variables[] = new String[] { "S", "S1", "A", "B", "V" };
		String terminals[] = new String[] { "a", "b" };
		String initialSymbol = "S";
		String rules[] = new String[] { "S -> AB | BS1 | BV", "S1 -> AB",
				"A -> BA | a", "B -> aa | VS1 | S1V | S1S1 | b", "V -> a" };
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}

	/*
	@Override
	protected void tearDown() throws Exception {
		Grammar g1 = (Grammar) this.g.clone();
		
		
		//tirar um el de g1 e ver se ele continua em g
		
	}
	*/

	@Test
	public void testClone() throws CloneNotSupportedException {
		Grammar gc = (Grammar) this.g.clone();
		
		assertEquals(5, gc.getVariables().size());
		assertEquals(2, gc.getTerminals().size());
		assertEquals("S", gc.getInitialSymbol());
		assertEquals(12, gc.getRules().size());
		
		this.g.getVariables().remove("S1");
		assertEquals(4, this.g.getVariables().size());
		assertEquals(5, gc.getVariables().size());
		
		this.g.getTerminals().remove("a");
		assertEquals(1, this.g.getTerminals().size());
		assertEquals(2, gc.getTerminals().size());
		
		this.g.setInitialSymbol("S'");
		assertEquals("S'", this.g.getInitialSymbol());
		assertEquals("S", gc.getInitialSymbol());
		
		
		Rule rx = new Rule("V","a");
		assertTrue(this.g.getRules().contains(rx));
		assertTrue(gc.getRules().contains(rx));
		
		for (Rule r : this.g.getRules()){
			r.setLeftSide("X" + r.getLeftSide());
			r.setRightSide(r.getRightSide() + "Y");
		}

		assertFalse(this.g.getRules().contains(rx));
		assertTrue(gc.getRules().contains(rx));
		
	}
	

}
