package br.com.lfaplataform.test;

import static org.junit.Assert.*;

import java.util.Set;

import junit.framework.*;
import br.com.lfaplataform.vo.*;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

public class Grammar05Test extends TestCase{
	
	private Grammar g;
	
	/*
	S -> AT | AB
	T -> XB
	X -> AT | AB
	A -> a
	B -> b
	*/
	
	@Override
	protected void setUp() throws Exception {		
		String[] variables = new String[]{"S", "T", "X", "A", "B"};
		String[] terminals = new String[]{"a", "b"};
		String initialSymbol = "S";
		String[] rules = new String[]{"S -> AT | AB", "T -> XB", "X -> AT | AB", "A -> a", "B -> b"};
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);		
	}
	
	@Test
	public void testInitialSymbolNotRecursive() {
		
		Grammar newG = GrammarParser.getGrammarWithInitialSymbolNotRecursive(this.g);
		
		String[] expectedVariables = new String[]{"S", "T", "X", "A", "B"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[]{"S -> AT | AB", "T -> XB", "X -> AT | AB", "A -> a", "B -> b"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));
	}

	@Test
	public void testGrammarEssentiallyNonContracting() {
		
		Grammar newG = GrammarParser.getGrammarEssentiallyNoncontracting(this.g);
		
		String[] expectedVariables = new String[]{"S", "T", "X", "A", "B"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[]{"S -> AT | AB", "T -> XB", "X -> AT | AB", "A -> a", "B -> b"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
	
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));		
	}

	@Test
	public void testChainRules() {
		Grammar newG = GrammarParser.getGrammarWithoutChainRules(this.g);
		
		String[] expectedVariables = new String[]{"S", "T", "X", "A", "B"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[]{"S -> AT | AB", "T -> XB", "X -> AT | AB", "A -> a", "B -> b"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));		
	}
	
	@Test
	public void  testNoTerminals() {
		Grammar newG = GrammarParser.getGrammarWithoutNoTerm(this.g);
		
		String[] expectedVariables = new String[]{"S", "T", "X", "A", "B"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[]{"S -> AT | AB", "T -> XB", "X -> AT | AB", "A -> a", "B -> b"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));			
	}
	
	
	
	
	@Test
	public void testCYK() {
		Set<String>[][] matrix = GrammarParser.CYK(g, "aaabbb");
		
		assertNotNull(matrix);
		assertNotNull(matrix[0][0]);
		assertNotEquals("", matrix[0][0]);
		
		Set<String> topVariables = matrix[0][0];
		
		assertEquals(2, topVariables.size());

		assertTrue(topVariables.contains("S"));
		assertTrue(topVariables.contains("X"));
	}
	
}
