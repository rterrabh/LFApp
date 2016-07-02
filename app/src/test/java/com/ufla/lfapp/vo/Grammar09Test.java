package com.ufla.lfapp.vo;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.Test;

public class Grammar09Test {

	private Grammar g;
	
	/*
	  A -> Aa | Ab | b | c	 
	 */
	
	@Before
	public void setUp() {
		String[] variables = new String[]{"A"};
		String[] terminals = new String[]{"a", "b", "c"};
		String initialSymbol = "A";
		String[] rules = new String[]{
				"A -> Aa | Ab | b | c" };
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);	
	}
	
	@Test
	public void testRemovingTheImmediateLeftRecursion() {
		
		Grammar newG = g.removingTheImmediateLeftRecursion(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[]{"A","Z1"};
		String[] expectedTerminals = new String[]{"a", "b", "c"};
		String expectedInitialSymbol = "A";
		String[] expectedRules = new String[]{
				"A -> bZ1 | cZ1 | b | c",
				"Z1 -> aZ1 | bZ1 | a | b" };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);		
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}	

}
