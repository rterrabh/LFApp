package com.ufla.lfapp.vo.grammar;

import com.ufla.lfapp.vo.grammar.AcademicSupport;
import com.ufla.lfapp.vo.grammar.Grammar;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.Test;

public class Grammar08Test {

	private Grammar g;
	
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
		
		String[] expectedVariables = new String[]{"A","Z1"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "A";
		String[] expectedRules = new String[]{
				"A -> bb | b | bbZ1 | bZ1",
				"Z1 -> aZ1 | abZ1 | a | ab" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}	

}
