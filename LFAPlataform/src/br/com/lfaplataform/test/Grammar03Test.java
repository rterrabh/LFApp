package br.com.lfaplataform.test;

import static org.junit.Assert.*;

import java.util.Set;

import br.com.lfaplataform.vo.*;
import junit.framework.*;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

public class Grammar03Test extends TestCase {
	
	private Grammar g;
	
	/*
	 S -> ABC | SA | A
	 A -> aA | a
	 B -> Sb | .
	 C -> cdC | dC | e
	*/
	
	@Override
	protected void setUp() throws Exception {
		String[] variables = new String[]{"S", "A", "B", "C"};
		String[] terminals = new String[]{"a", "b", "c", "d", "e"};
		String initialSymbol = "S";
		String[] rules = new String[]{"S -> ABC | SA | A", "A -> aA | a", "B -> Sb | .", "C -> cdC | dC | e"};
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}
	
	@Test
	public void testInitialSymbolNotRecursive() {
		
		Grammar newG = GrammarParser.getGrammarWithInitialSymbolNotRecursive(this.g);
		
		String[] expectedVariables = new String[]{"S'", "S", "A", "B", "C"};
		String[] expectedTerminals = new String[]{"a", "b", "c", "d", "e"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[]{"S' -> S", "S -> ABC | SA | A", "A -> aA | a", "B -> Sb | .", "C -> cdC | dC | e"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));
	}
	
	@Test
	public void testGrammarEssentiallyNonContracting() {
		
		Grammar newG = GrammarParser.getGrammarEssentiallyNoncontracting(this.g);
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b", "c", "d", "e"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> ABC | SA | AC | A ", "A -> aA | a", "B -> Sb", "C -> cdC | dC | e"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
				
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));		
	}
	
	@Test
	public void testChainRules() {
		Grammar newG = GrammarParser.getGrammarWithoutChainRules(this.g);
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b", "c", "d", "e"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> ABC | SA | aA | a", "A -> aA | a", "B -> Sb | .", "C -> cdC | dC | e"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));		
	}
	
	@Test
	public void  testNoTerminals() {
		Grammar newG = GrammarParser.getGrammarWithoutNoTerm(this.g);
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b", "c", "d", "e"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> ABC | SA | A", "A -> aA | a", "B -> Sb | .", "C -> cdC | dC | e"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));			
	}
	
	@Test
	public void testNoReach() {
		Grammar newG = GrammarParser.getGrammarWithoutNoReach(this.g);
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b", "c", "d", "e"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> ABC | SA | A", "A -> aA | a", "B -> Sb | .", "C -> cdC | dC | e"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));
	}
	
	@Test
	public void testFNC() {
		Grammar newG = GrammarParser.FNC(this.g);
		
		String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C", "T1", "T2", "T3", "T4", "T5", "T6", "T7"};
		String[] expectedTerminals = new String[] {"a", "b", "c", "d", "e"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {"S' -> T1A | AC | AT6 |  a | SA", "S -> SA | AC | a | AT7 | T1A", "A -> T1A | a", "B -> ST2", "C -> T4T5 | T3C | e",
				"T1 -> a", "T2 -> b", "T3 -> d", "T4 -> c", "T5 -> T3C", "T6 -> BC", "T7 -> BC"};		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));	
	}
	
	/*@Test
	public void testCYK() {
		Set<String>[][] matrix = GrammarParser.CYK(g, "aaabbbb");
		
		assertNotNull(matrix);
		assertNotNull(matrix[0][0]);
		assertNotEquals("", matrix[0][0]);
		
		Set<String> topVariables = matrix[0][0];
		
		assertEquals(0, topVariables.size());

		assertTrue(topVariables.isEmpty());
		
	}*/
	
	

}
