package vo;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.*;
import org.junit.Test;

public class Grammar02Test {

	private Grammar g;
	
	/*
	 Grammar:
	 S -> aS | AB | AC
	 A -> aA | .
	 B -> bB | b
	 C -> cC | .
	 */
	
	@Before
	public void setUp() {
		String[] variables = new String[] {"S", "A", "B", "C"};
		String[] terminals = new String[] {"a", "b", "c"};
		String initialSymbol = "S";
		String[] rules = new String[] {
				"S -> aS | AB | AC",
				"A -> aA | .",
				"B -> bB | b",
				"C -> cC | ." };
		
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
				"A -> aA | .",
				"B -> bB | b",
				"C -> cC | ." };
		
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
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | AB | AC | a | B | A | C | .",
				"A -> aA | a",
				"B -> bB | b",
				"C -> cC | c" };
		
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
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | AB | AC",
				"A -> aA | .",
				"B -> bB | b",
				"C -> cC | ." };
		
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
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | AB | AC",
				"A -> aA | .",
				"B -> bB | b",
				"C -> cC | ." };
		
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
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> aS | AB | AC",
				"A -> aA | .",
				"B -> bB | b",
				"C -> cC | ." };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}
	
	@Test
	public void testFNC() {
		Grammar newG = g.FNC(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C", "T1", "T2", "T3"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {
				"S'-> T3B | T1S | T1A | c | T2C | AC | AB | . | b | a",
				"S -> T3B | T1S | T1A | c | T2C | AC | AB | b | a",
				"A -> a | T1A", "B -> T3B | b", "C -> T2C | c",
				"T1 -> a",
				"T2 -> c",
				"T3 -> b" };

		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);

		
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
		Set<String>[][] matrix = Grammar.CYK(g, "bbb");
		
		assertNotNull(matrix);
		assertNotNull(matrix[0][0]);
		assertNotEquals("", matrix[0][0]);
		
		Set<String> topVariables = matrix[0][0];
		
		assertEquals(0, topVariables.size());

		assertTrue(topVariables.isEmpty());
		//assertTrue(topVariables.contains("S"));
	}

}
