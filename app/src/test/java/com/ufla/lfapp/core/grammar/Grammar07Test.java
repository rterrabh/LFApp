package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.utils.ResourcesContext;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.Test;

public class Grammar07Test {
	
	private Grammar g;

	static {
		ResourcesContext.isTest = true;
	}

	/*
	 A -> AB | BA | a
	 B -> b | c
	  */
	
	@Before
	public void setUp() {
		String[] variables = new String[]{"A", "B"};
		String[] terminals = new String[]{"a", "b", "c"};
		String initialSymbol = "A";
		String[] rules = new String[]{
				"A -> AB | BA | a",
				"B -> b | c" };
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);		
	}

	@Test
	public void testInitialSymbolNotRecursive() {
		Grammar newG = g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[]{"A'", "A", "B"};
		String[] expectedTerminals = new String[]{"a", "b", "c"};
		String expectedInitialSymbol = "A'";
		String[] expectedRules = new String[]{
				"A' -> A",
				"A -> AB | BA | a",
				"B -> b | c" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}
	
	
	@Test
	public void testGrammarEssentiallyNonContracting() {
		Grammar newG = g.getGrammarEssentiallyNoncontracting(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[] {"A", "B"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "A";
		String[] expectedRules = new String[] {
				"A -> AB | BA | a",
				"B -> b | c" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());		
	}
	
	
	@Test
	public void testChainRules() {
		Grammar newG = g.getGrammarWithoutChainRules(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[] {"A", "B"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "A";
		String[] expectedRules = new String[] {
				"A -> AB | BA | a",
				"B -> b | c" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());		
	}
	
	@Test
	public void  testNoTerminals() {
		Grammar newG = g.getGrammarWithoutNoTerm(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[] {"A", "B"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "A";
		String[] expectedRules = new String[] {
				"A -> AB | BA | a",
				"B -> b | c" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());			
	}
	
	@Test
	public void testNoReach() {
		Grammar newG = g.getGrammarWithoutNoReach(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[] {"A", "B"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "A";
		String[] expectedRules = new String[] {
				"A -> AB | BA | a",
				"B -> b | c" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}
	
	
	@Test
	public void testFNC() {
		Grammar newG = g.FNC(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[] {"A'","A", "B"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "A'";
		String[] expectedRules = new String[] {
				"A' -> AB | BA | a",
				"A -> AB | BA | a",
				"B -> b | c" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());	
	}
	
	@Test
	public void testRemovingTheImmediateLeftRecursion() {
		Grammar newG = g.removingTheImmediateLeftRecursion(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[]{"A", "B", "R1"};
		String[] expectedTerminals = new String[]{"a", "b", "c"};
		String expectedInitialSymbol = "A";
		String[] expectedRules = new String[]{
				"A -> BAR1 | aR1 | BA | a",
				"B -> b | c",
				"R1 -> BR1 | B" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}

}
