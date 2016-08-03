package com.ufla.lfapp.vo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CloneTest {

	private Grammar g;
	//"[["+TERMINAL+"]*"+"["+VARIABLE+"]*]*";
	private static final String VARIABLE = "[A-Z][0-9]*'?";
	private static final String TERMINAL = "[a-z]";
	private static final String RULE_ELEMENT =
			"["+TERMINAL+VARIABLE+"]*";

	@Test
	public void test() {
		String teste = "A";
		Pattern p = Pattern.compile(RULE_ELEMENT);
		System.out.println(p.matcher("A").matches());
		System.out.println(p.matcher("1E").matches());
		System.out.println(p.matcher("G1").matches());
		System.out.println(p.matcher("Z55332").matches());
		System.out.println(p.matcher("Z''''").matches());
		System.out.println(p.matcher("Z'").matches());
	}

	@Before
	public void setUp() {
		String variables[] = new String[] { "S", "S1", "A", "B", "V" };
		String terminals[] = new String[] { "a", "b" };
		String initialSymbol = "S";
		String rules[] = new String[] {
				"S -> AB | BS1 | BV",
				"S1 -> AB",
				"A -> BA | a",
				"B -> aa | VS1 | S1V | S1S1 | b",
				"V -> a" };
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}

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
