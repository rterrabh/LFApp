package vo;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.Test;

public class Grammar07Test {
	
	private Grammar g;
	
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
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
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
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
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
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
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
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
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
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}
	
	
	@Test
	public void testNFC() {
		Grammar newG = g.FNC(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[] {"A'","A", "B"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "A'";
		String[] expectedRules = new String[] {
				"A' -> AB | BA | a",
				"A -> AB | BA | a",
				"B -> b | c" };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
//		System.out.println("________________________________");
//		for (Rule element : newG.getRules()) {
//			System.out.println(element.getLeftSide() + "->" + element.getRightSide());
//		}
//		System.out.println("_______________________________");
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());	
	}
	
	@Test
	public void testRemovingTheImmediateLeftRecursion() {
		
		Grammar newG = g.removingTheImmediateLeftRecursion(this.g, new AcademicSupport());
		
		String[] expectedVariables = new String[]{"A", "B", "Z1"};
		String[] expectedTerminals = new String[]{"a", "b", "c"};
		String expectedInitialSymbol = "A";
		String[] expectedRules = new String[]{
				"A -> BAZ1 | aZ1 | BA | a",
				"B -> b | c",
				"Z1 -> BZ1 | B" };
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);		
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(expectedGrammar.getTerminals(), newG.getTerminals());
		
		assertEquals(expectedGrammar.getRules(), newG.getRules());
		
		assertEquals(expectedGrammar.getVariables(), newG.getVariables());
	}

}
