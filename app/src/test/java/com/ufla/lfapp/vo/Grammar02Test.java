package com.ufla.lfapp.vo;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.*;
import org.junit.Test;

public class Grammar02Test {

	private Grammar g;
	
	/*
	 Grammar:
	 S -> aS | AB | AC
	 A -> aA | λ
	 B -> bB | b
	 C -> cC | λ
	 */
	
	@Before
	public void setUp() {
		String[] variables = new String[] {"S", "A", "B", "C"};
		String[] terminals = new String[] {"a", "b", "c"};
		String initialSymbol = "S";
		String[] rules = new String[] {
				"S -> aS | AB | AC",
				"A -> aA | λ",
				"B -> bB | b",
				"C -> cC | λ" };
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}


	@Test
	public void testInitialSymbolNotRecursive() {
		Grammar newG = g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {
				"S' -> S",
				"S -> aS | AB | AC",
				"A -> aA | λ",
				"B -> bB | b",
				"C -> cC | λ" };
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
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | AB | AC | a | B | A | C | λ",
				"A -> aA | a",
				"B -> bB | b",
				"C -> cC | c" };
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
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | AB | AC",
				"A -> aA | λ",
				"B -> bB | b",
				"C -> cC | λ" };
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
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | AB | AC",
				"A -> aA | λ",
				"B -> bB | b",
				"C -> cC | λ" };
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
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | AB | AC",
				"A -> aA | λ",
				"B -> bB | b",
				"C -> cC | λ" };
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

		String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {
				"S' -> S | λ",
				"S -> aS | AB | AC | a | B | A | C",
				"A -> aA | a",
				"B -> bB | b",
				"C -> cC | c" };
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

		String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {
				"S' -> aS | AB | AC | a | aA | bB | b | cC | c | λ",
				"S -> aS | AB | AC | a | aA | bB | b | cC | c",
				"A -> aA | a",
				"B -> bB | b",
				"C -> cC | c" };
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

		String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {
				"S' -> aS | AB | AC | a | aA | bB | b | cC | c | λ",
				"S -> aS | AB | AC | a | aA | bB | b | cC | c",
				"A -> aA | a",
				"B -> bB | b",
				"C -> cC | c" };
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

		String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {
				"S' -> aS | AB | AC | a | aA | bB | b | cC | c | λ",
				"S -> aS | AB | AC | a | aA | bB | b | cC | c",
				"A -> aA | a",
				"B -> bB | b",
				"C -> cC | c" };
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

		String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C", "A'", "B'", "C'"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {
				"S' -> A'S | AB | AC | a | A'A | B'B | b | C'C | c | λ",
				"S -> A'S | AB | AC | a | A'A | B'B | b | C'C | c",
				"A -> A'A | a",
				"B -> B'B | b",
				"C -> C'C | c",
				"A' -> a",
				"B' -> b",
				"C' -> c", };
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
		for (com.ufla.lfapp.vo.Rule element : newG.getRules()) {
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
	
	@Test
	public void testCYK() {
		Grammar newG = g.FNC(g, new AcademicSupport());
		Set<String>[][] matrix = Grammar.CYK(newG, "bbb");

		assertNotNull(matrix);
		assertNotNull(matrix[0][0]);
		assertNotEquals("", matrix[0][0]);
		
		Set<String> topVariables = matrix[0][0];

		assertTrue(topVariables.contains(newG. getInitialSymbol()));
	}

}
