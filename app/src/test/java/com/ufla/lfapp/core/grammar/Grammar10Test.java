package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.utils.ResourcesContext;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.Test;

public class Grammar10Test {

	private Grammar g;

	static {
		ResourcesContext.isTest = true;
	}

	/*
	  S -> aS | bB
	  B -> aEE | CDE 
	  C -> aBa | D 
	  D -> aEa | λ
	  E -> aDD | DC
	 */
	
	@Before
	public void setUp() {
		String[] variables = new String[]{"S", "B", "C", "D", "E"};
		String[] terminals = new String[]{"a", "b"};
		String initialSymbol = "S";
		String[] rules = new String[]{
				"S -> aS | bB",
				"B -> aEE | CDE",
				"C -> aBa | D ",
				"D -> aEa | λ",
				"E -> aDD | DC"  };
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);	
	}
	
	@Test
	public void testInitialSymbolNotRecursive() {
		Grammar newG = g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[]{"S'", "S", "B", "C", "D", "E"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[]{
				"S' -> S",
				"S -> aS | bB",
				"B -> aEE | CDE",
				"C -> aBa | D ",
				"D -> aEa | λ",
				"E -> aDD | DC" };
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
		
		String[] expectedVariables = new String[]{"S", "B", "C", "D", "E"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[]{
				"S -> aS | bB | b",
				"B -> aEE | aE | a | CDE | CD | CE | DE | C | D | E",
				"C -> aBa | aa | D",
				"D -> aEa | aa",
				"E -> aDD | aD | a | DC | D | C" };
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
		
		String[] expectedVariables = new String[]{"S", "B", "C", "D", "E"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[]{
				"S -> aS | bB",
				"B -> aEE | CDE",
				"C -> aBa | aEa | λ ",
				"D -> aEa | λ",
				"E -> aDD | DC" };
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
		
		String[] expectedVariables = new String[] {"S", "B", "C", "D", "E"};
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | bB",
				"B -> aEE | CDE",
				"C -> aBa | D ",
				"D -> aEa | λ",
				"E -> aDD | DC" };
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
		
		String[] expectedVariables = new String[] {"S", "B", "C", "D", "E"};
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | bB",
				"B -> aEE | CDE",
				"C -> aBa | D ",
				"D -> aEa | λ",
				"E -> aDD | DC" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}

	@Test
	public void testPreFNC2() {
		Grammar newG = this.g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
		newG = newG.getGrammarEssentiallyNoncontracting(newG, new AcademicSupport());

		String[] expectedVariables = new String[]{"S'", "S", "B", "C", "D", "E"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[]{
				"S' -> S",
				"S  -> aS | bB | b",
				"B  -> aEE | aE | a | CDE | CD | CE | DE | C | D | E",
				"C  -> aBa | aa | D",
				"D  -> aEa | aa",
				"E  -> aDD | aD | a | DC | D | C" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);

		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}

	@Test
	public void testPreFNC3() {
		Grammar newG = this.g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
		newG = newG.getGrammarEssentiallyNoncontracting(newG, new AcademicSupport());
		newG = newG.getGrammarWithoutChainRules(newG, new AcademicSupport());

		String[] expectedVariables = new String[]{"S'", "S", "B", "C", "D", "E"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[]{
				"S' -> aS | bB | b",
				"S  -> aS | bB | b",
				"B  -> aEE | aE | a | CDE | CD | CE | DE | aBa | aa | aEa | aDD | aD | DC",
				"C  -> aBa | aa | aEa",
				"D  -> aEa | aa",
				"E  -> aDD | aD | a | DC | aEa | aa | aBa" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);

		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}

	@Test
	public void testPreFNC4() {
		Grammar newG = this.g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
		newG = newG.getGrammarEssentiallyNoncontracting(newG, new AcademicSupport());
		newG = newG.getGrammarWithoutChainRules(newG, new AcademicSupport());
		newG = newG.getGrammarWithoutNoTerm(newG, new AcademicSupport());

		String[] expectedVariables = new String[]{"S'", "S", "B", "C", "D", "E"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[]{
				"S' -> aS | bB | b",
				"S -> aS | bB | b",
				"B -> aEE | aE | a | CDE | CD | CE | DE | aBa | aa | aEa | aDD | aD | DC",
				"C -> aBa | aa | aEa",
				"D -> aEa | aa",
				"E -> aDD | aD | a | DC | aEa | aa | aBa" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);

		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}

	@Test
	public void testPreFNCComplete() {
		Grammar newG = this.g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
		newG = newG.getGrammarEssentiallyNoncontracting(newG, new AcademicSupport());
		newG = newG.getGrammarWithoutChainRules(newG, new AcademicSupport());
		newG = newG.getGrammarWithoutNoTerm(newG, new AcademicSupport());
		newG = newG.getGrammarWithoutNoReach(newG, new AcademicSupport());

		String[] expectedVariables = new String[]{"S'", "S", "B", "C", "D", "E"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[]{
				"S' -> aS | bB | b",
				"S -> aS | bB | b",
				"B -> aEE | aE | a | CDE | CD | CE | DE | aBa | aa | aEa | aDD | aD | DC",
				"C -> aBa | aa | aEa",
				"D -> aEa | aa",
				"E -> aDD | aD | a | DC | aEa | aa | aBa" };
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
		String[] expectedVariables = new String[]{"S'", "S", "B", "C", "D", "E", "A'", "B'", "T1",
				"T2", "T3", "T4", "T5" };
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[]{
				"S' -> A'S | B'B | b",
				"S  -> A'S | B'B | b",
				"B  -> A'T4 | A'E | a | CT5 | CD | CE | DE | A'T1 | A'A' | A'T3 | A'T2 | A'D | DC",
				"C  -> A'T1 | A'A' | A'T3",
				"D  -> A'T3 | A'A'",
				"E  -> A'T2 | A'D | a | DC | A'T3 | A'A' | A'T1",
				"A' -> a",
				"B' -> b",
				"T1 -> BA'",
				"T2 -> DD",
				"T3 -> EA'",
				"T4 -> EE",
				"T5 -> DE", };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);

		// Algoritmo MODIFICADO
		//assertEquals(expectedGrammar.getRules(), newG.getRules());
		String initialSymbol = newG.getInitialSymbol();
		for (Rule rule : newG.getRules()) {
			assertTrue(rule.isFnc(initialSymbol));
		}
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}

	@Test
	public void testFNG() {
		Grammar newG = g.FNGTerra(g, new AcademicSupport());
		boolean fng = true;
		for (com.ufla.lfapp.core.grammar.Rule element : newG.getRules()) {
			int counter = 0;
			if (!element.getLeftSide().equals(newG.getInitialSymbol()) &&
					element.getRightSide().equals("")) {
				fng = false;
			} else {
				for (int i = 0; i < element.getRightSide().length() && fng; i++) {
					if (Character.isLowerCase(element.getRightSide().charAt(i))) {
						counter++;
					}
				}
				if (counter > 1) {
					fng = false;
				}
			}
		}

		assertEquals(true, fng);
		assertTrue(newG.isFNG());
	}

}
