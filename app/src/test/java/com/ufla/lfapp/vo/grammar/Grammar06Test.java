package com.ufla.lfapp.vo.grammar;

import com.ufla.lfapp.vo.grammar.*;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.Test;

public class Grammar06Test {
	
	private Grammar g;

	/*
	S -> AB | λ
	A -> AB | CB | a
	B -> AB | b
	C -> AC | c 	 
	 */
	
	@Before
	public void setUp() {
		String[] variables = new String[]{"S", "A", "B", "C"};
		String[] terminals = new String[]{"a","c", "b"};
		String initialSymbol = "S";
		String[] rules = new String[]{
				"S -> AB | λ",
				"A -> AB | CB | a",
				"B -> AB | b",
				"C -> AC | c" };
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}
	
	
	@Test
	public void testInitialSymbolNotRecursive() {
		Grammar newG = g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[]{"S", "A", "B", "C"};
		String[] expectedTerminals = new String[]{"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[]{
				"S -> AB | λ",
				"A -> AB | CB | a",
				"B -> AB | b",
				"C -> AC | c" };
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
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> AB | λ",
				"A -> AB | CB | a",
				"B -> AB | b",
				"C -> AC | c" };
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
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> AB | λ",
				"A -> AB | CB | a",
				"B -> AB | b",
				"C -> AC | c" };
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
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> AB | λ",
				"A -> AB | CB | a",
				"B -> AB | b",
				"C -> AC | c" };
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
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> AB | λ",
				"A -> AB | CB | a",
				"B -> AB | b",
				"C -> AC | c" };
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

		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> AB | λ",
				"A -> AB | CB | a",
				"B -> AB | b",
				"C -> AC | c" };
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

		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> AB | λ",
				"A -> AB | CB | a",
				"B -> AB | b",
				"C -> AC | c" };
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

		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> AB | λ",
				"A -> AB | CB | a",
				"B -> AB | b",
				"C -> AC | c" };
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

		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> AB | λ",
				"A -> AB | CB | a",
				"B -> AB | b",
				"C -> AC | c" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);

		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}

	@Test
	public void testPreFNC() {
		Grammar newG = this.g.FNC(this.g, new AcademicSupport());

		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> AB | λ",
				"A -> AB | CB | a",
				"B -> AB | b",
				"C -> AC | c" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);

		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}
	
	@Test
	public void testFNG() {
		Grammar newG = g.FNG(g, new AcademicSupport());
		boolean fng = true;
		for (com.ufla.lfapp.vo.grammar.Rule element : newG.getRules()) {
			int counter = 0;
			if (!element.getLeftSide().equals(newG.getInitialSymbol()) &&
					element.getRightSide().equals("")) {
				fng = false;
				//counter = 1;
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
