package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.utils.ResourcesContext;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.Test;

public class Grammar08Test {

	private Grammar g;

	static {
		ResourcesContext.isTest = true;
	}
	
	/*
	  A -> Aa | Aab | bb | b	 
	 */
	
	@Before
	public void setUp() {
		String[] variables = new String[]{"A"};
		String[] terminals = new String[]{"a", "b"};
		String initialSymbol = "A";
		String[] rules = new String[]{
				"A -> Aa | Aab | bb | b" };
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);	
	}
	
	@Test
	public void testRemovingTheImmediateLeftRecursion() {
		Grammar newG = g.removingTheImmediateLeftRecursion(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[]{"A","R1"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "A";
		String[] expectedRules = new String[]{
				"A -> bb | b | bbR1 | bR1",
				"R1 -> aR1 | abR1 | a | ab" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}	

}
