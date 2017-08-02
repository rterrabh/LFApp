package com.ufla.lfapp.core.grammar;

import com.ufla.lfapp.utils.ResourcesContext;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Set;

import org.junit.*;
import org.junit.Test;

public class Grammar04Test {
	
	private Grammar g;

	static {
		ResourcesContext.isTest = true;
	}

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
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | ABC | a | AB | AC | BC | A | B | C | λ",
				"A -> BB | B",
				"B -> CC | a | C",
				"C -> AA | b | A" };
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
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | ABC",
				"A -> BB | λ",
				"B -> CC | a",
				"C -> AA | b" };
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
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | ABC",
				"A -> BB | λ",
				"B -> CC | a",
				"C -> AA | b" };
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
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | ABC",
				"A -> BB | λ",
				"B -> CC | a",
				"C -> AA | b" };
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
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {
				"S' -> S | λ",
				"S -> aS | ABC | a | AB | AC | BC | A | B | C",
				"A -> BB | B",
				"B -> CC | a | C",
				"C -> AA | b | A" };
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
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {
				"S' -> aS | ABC | a | AB | AC | BC | BB | CC | AA | b | λ",
				"S -> aS | ABC | a | AB | AC | BC | BB | CC | AA | b",
				"A -> BB | CC | a | AA | b",
				"B -> CC | a | AA | b | BB",
				"C -> AA | b | BB | CC | a" };
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
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {
				"S' -> aS | ABC | a | AB | AC | BC | BB | CC | AA | b | λ",
				"S -> aS | ABC | a | AB | AC | BC | BB | CC | AA | b",
				"A -> BB | CC | a | AA | b",
				"B -> CC | a | AA | b | BB",
				"C -> AA | b | BB | CC | a" };
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
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {
				"S' -> aS | ABC | a | AB | AC | BC | BB | CC | AA | b | λ",
				"S -> aS | ABC | a | AB | AC | BC | BB | CC | AA | b",
				"A -> BB | CC | a | AA | b",
				"B -> CC | a | AA | b | BB",
				"C -> AA | b | BB | CC | a" };
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

		String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C", "A'", "T1"};
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {
				"S' -> A'S | AT1 | a | AB | AC | BC | BB | CC | AA | b | λ",
				"S -> A'S | AT1 | a | AB | AC | BC | BB | CC | AA | b",
				"A -> BB | CC | a | AA | b",
				"B -> CC | a | AA | b | BB",
				"C -> AA | b | BB | CC | a",
				"A' -> a",
				"T1 -> BC"};
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals,
				expectedInitialSymbol, expectedRules);

		assertEquals(expectedGrammar.getRules(), newG.getRules());
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	} 
	
	@Test
	public void testFNG() {
		Grammar newG = g.FNC(g, new AcademicSupport());
		AcademicSupport academicSupport = new AcademicSupport();
		Grammar newG2 = newG.removingLeftRecursionTerra(newG, academicSupport,
				new HashMap<String, String>(), new AcademicSupportForRemoveLeftRecursion());
		newG = newG.FNGTerra(newG, new AcademicSupport());
		boolean fng = true;
		for (Rule element : newG.getRules()) {
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
		g = g.FNC(g, new AcademicSupport());
		Set<String>[][] matrix = Grammar.CYK(g, "bbabaa");
		
		assertNotNull(matrix);
		assertNotNull(matrix[0][0]);
		assertNotEquals("", matrix[0][0]);
		
		Set<String> topVariables = matrix[0][0];

		assertTrue(topVariables.contains(g.getInitialSymbol()));
	}

}
