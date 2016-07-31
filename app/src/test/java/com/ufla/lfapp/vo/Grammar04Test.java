package com.ufla.lfapp.vo;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Set;

import org.junit.*;
import org.junit.Test;

public class Grammar04Test {
	
	private Grammar g;
	
	/* Grammar:
	 S -> aS | ABC
	 A -> BB | λ
	 B -> CC | a
	 C -> AA | b
	 */
	
	@Before
	public void setUp() {
		String[] variables = new String[]{"S", "A", "B", "C"};
		String[] terminals = new String[]{"a", "b"};
		String initialSymbol = "S";
		String[] rules = new String[]{
				"S -> aS | ABC",
				"A -> BB | λ",
				"B -> CC | a",
				"C -> AA | b" };
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}
	
	@Test
	public void testInitialSymbolNotRecursive() {
		
		Grammar newG = g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[]{"S'", "S", "A", "B", "C"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[]{
				"S' -> S",
				"S -> aS | ABC",
				"A -> BB | λ",
				"B -> CC | a",
				"C -> AA | b" };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}

	@Test
	public void testGrammarEssentiallyNonContracting() {

		Grammar newG = g.getGrammarEssentiallyNoncontracting(this.g, new AcademicSupport());

		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | ABC | a | AB | AC | BC | A | B | C | λ",
				"A -> BB | B",
				"B -> CC | a | C",
				"C -> AA | b | A" };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);

		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
	
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}
	
	@Test
	public void testChainRules() {
		Grammar newG = g.getGrammarWithoutChainRules(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | ABC",
				"A -> BB | λ",
				"B -> CC | a",
				"C -> AA | b" };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}
	
	@Test
	public void  testNoTerminals() {
		Grammar newG = g.getGrammarWithoutNoTerm(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | ABC",
				"A -> BB | λ",
				"B -> CC | a",
				"C -> AA | b" };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}
	
	@Test
	public void testNoReach() {
		Grammar newG = g.getGrammarWithoutNoReach(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | ABC",
				"A -> BB | λ",
				"B -> CC | a",
				"C -> AA | b" };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}
	
	@Test
	public void testFNC() {
		Grammar newG = g.FNC(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C", "T1", "T3"};
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {
				"S' -> λ | AA | AB | AT1 | AC | BB | BC | CC | a | T3S | b",
				"S -> AA | AB | AT1 | AC | BB | BC | CC | a | T3S | b",
				"A -> a | b | AA | CC | BB",
				"B -> AA | BB | CC | a | b",
				"C -> CC | b | a | BB | AA",
				"T1 -> BC",
				"T3 -> a" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);

		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	} 
	
	//@Test
	public void testFNG() {
		Grammar newG = g.FNC(g, new AcademicSupport());
		AcademicSupport academicSupport = new AcademicSupport();
		Grammar newG2 = newG.removingLeftRecursion(newG, academicSupport,
				new HashMap<String, String>(), new AcademicSupportForRemoveLeftRecursion());
		System.out.println(newG2.toStringRulesMapVToU());
		System.out.println(academicSupport.getSolutionDescription());
		newG = newG.FNG(newG, new AcademicSupport());
		boolean fng = true;
		for (com.ufla.lfapp.vo.Rule element : newG.getRules()) {
			int counter = 0;
			if (!element.getLeftSide().equals(newG.getInitialSymbol()) && element.getRightSide().equals("")) {
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

		System.out.println(newG.toStringRulesMapVToU());
		
		assertEquals(true, fng);
		assertTrue(newG.isFNG());
	}
	
	@Test
	public void testCYK() {
		g = g.FNC(g, new AcademicSupport());
		Set<String>[][] matrix = Grammar.CYK(g, "bbabaa");
		
		assertNotNull(matrix);
		assertNotNull(matrix[0][0]);
		assertNotEquals("", matrix[0][0]);
		
		Set<String> topVariables = matrix[0][0];


		assertTrue(topVariables.contains(g.getInitialSymbol()));
	}

}
