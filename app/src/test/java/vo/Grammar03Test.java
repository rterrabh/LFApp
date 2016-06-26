package vo;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.*;
import org.junit.Test;

public class Grammar03Test {
	
	private Grammar g;
	
	/*
	 S -> ABC | SA | A
	 A -> aA | a
	 B -> Sb | .
	 C -> cdC | dC | e
	*/
	
	@Before
	public void setUp() {
		String[] variables = new String[]{"S", "A", "B", "C"};
		String[] terminals = new String[]{"a", "b", "c", "d", "e"};
		String initialSymbol = "S";
		String[] rules = new String[]{
				"S -> ABC | SA | A",
				"A -> aA | a",
				"B -> Sb | .",
				"C -> cdC | dC | e" };
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}
	
	@Test
	public void testInitialSymbolNotRecursive() {
		
		Grammar newG = g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[]{"S'", "S", "A", "B", "C"};
		String[] expectedTerminals = new String[]{"a", "b", "c", "d", "e"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[]{
				"S' -> S",
				"S -> ABC | SA | A",
				"A -> aA | a",
				"B -> Sb | .",
				"C -> cdC | dC | e" };
		
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
		String[] expectedTerminals = new String[] {"a", "b", "c", "d", "e"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> ABC | SA | AC | A ",
				"A -> aA | a",
				"B -> Sb",
				"C -> cdC | dC | e" };
		
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
		String[] expectedTerminals = new String[] {"a", "b", "c", "d", "e"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> ABC | SA | aA | a",
				"A -> aA | a", "B -> Sb | .",
				"C -> cdC | dC | e" };
		
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
		String[] expectedTerminals = new String[] {"a", "b", "c", "d", "e"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> ABC | SA | A",
				"A -> aA | a",
				"B -> Sb | .",
				"C -> cdC | dC | e" };
		
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
		String[] expectedTerminals = new String[] {"a", "b", "c", "d", "e"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> ABC | SA | A",
				"A -> aA | a",
				"B -> Sb | .",
				"C -> cdC | dC | e" };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}
	
	@Test
	public void testFNC() {
		Grammar newG = g.FNC(this.g, new AcademicSupport());

		String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C",
				"T1", "T2", "T3", "T4", "T5", "T6"};
		String[] expectedTerminals = new String[] {"a", "b", "c", "d", "e"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {
				"S' -> AT5 | AC | SA | a | T1A",
				"S -> AT5 | AC | SA | a | T1A",
				"A -> a | T1A",
				"B -> ST6",
				"C -> T2T4 | T3C | e",
				"T1 -> a",
				"T2 -> c",
				"T3 -> d",
				"T4 -> T3C",
				"T5 -> BC",
				"T6 -> b" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);

		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}
	
	@Test
	public void testFNG() {
		Grammar newG = g.FNG(g, new AcademicSupport());
		boolean fng = true;
		for (Rule element : newG.getRules()) {
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
		
		assertEquals(true, fng);
	}
	
	@Test
	public void testCYK() {
		Set<String>[][] matrix = Grammar.CYK(g, "aaabbbb");
		
		assertNotNull(matrix);
		assertNotNull(matrix[0][0]);
		assertNotEquals("", matrix[0][0]);
		
		Set<String> topVariables = matrix[0][0];
		
		assertEquals(0, topVariables.size());

		assertTrue(topVariables.isEmpty());
		
	}

}
