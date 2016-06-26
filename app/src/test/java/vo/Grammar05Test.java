package vo;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.*;
import org.junit.Test;

public class Grammar05Test {
	
	private Grammar g;
	
	/*
	S -> AT | AB
	T -> XB
	X -> AT | AB
	A -> a
	B -> b
	*/
	
	@Before
	public void setUp() {
		String[] variables = new String[]{"S", "T", "X", "A", "B"};
		String[] terminals = new String[]{"a", "b"};
		String initialSymbol = "S";
		String[] rules = new String[]{
				"S -> AT | AB",
				"T -> XB",
				"X -> AT | AB",
				"A -> a",
				"B -> b" };
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);		
	}
	
	@Test
	public void testInitialSymbolNotRecursive() {
		
		Grammar newG = g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[]{"S", "T", "X", "A", "B"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[]{
				"S -> AT | AB",
				"T -> XB",
				"X -> AT | AB",
				"A -> a",
				"B -> b" };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}

	@Test
	public void testGrammarEssentiallyNonContracting() {
		
		Grammar newG = g.getGrammarEssentiallyNoncontracting(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[]{"S", "T", "X", "A", "B"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[]{
				"S -> AT | AB",
				"T -> XB",
				"X -> AT | AB",
				"A -> a",
				"B -> b" };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
	
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());		
	}

	@Test
	public void testChainRules() {
		Grammar newG = g.getGrammarWithoutChainRules(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[]{"S", "T", "X", "A", "B"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[]{
				"S -> AT | AB",
				"T -> XB",
				"X -> AT | AB",
				"A -> a",
				"B -> b" };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());		
	}
	
	@Test
	public void  testNoTerminals() {
		Grammar newG = g.getGrammarWithoutNoTerm(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[]{"S", "T", "X", "A", "B"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[]{
				"S -> AT | AB",
				"T -> XB",
				"X -> AT | AB",
				"A -> a",
				"B -> b" };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());			
	}
	
	@Test
	public void testFNC() {
		Grammar newG = g.FNC(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[] {"S", "T", "X", "A", "B"};
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {
				"S -> AB | AT",
				"X -> AB | AT",
				"T -> XB",
				"B -> b",
				"A -> a" };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
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
		Set<String>[][] matrix = Grammar.CYK(g, "aaabbb");
		
		assertNotNull(matrix);
		assertNotNull(matrix[0][0]);
		assertNotEquals("", matrix[0][0]);
		
		Set<String> topVariables = matrix[0][0];
		
		assertEquals(2, topVariables.size());

		assertTrue(topVariables.contains("S"));
		assertTrue(topVariables.contains("X"));
	}
	
}
