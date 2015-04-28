package br.com.lfaplataform.test;

import static org.junit.Assert.*;
import junit.framework.*;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import br.com.lfaplataform.vo.*;

public class Grammar06Test extends TestCase {
	
	private Grammar g;

	/*
	 S → AB | .
	A → AB | CB | a
	B → AB | b
	C → AC | c 	 
	 */
	
	@Override
	protected void setUp() throws Exception {
		String[] variables = new String[]{"S", "A", "B", "C"};
		String[] terminals = new String[]{"a","c", "b"};
		String initialSymbol = "S";
		String[] rules = new String[]{"S -> AB | .", "A -> AB | CB | a", "B -> AB | b", "C -> AC | c"};
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}
	
	
	@Test
	public void testInitialSymbolNotRecursive() {
		
		Grammar newG = GrammarParser.getGrammarWithInitialSymbolNotRecursive(this.g);
		
		String[] expectedVariables = new String[]{"S", "A", "B", "C"};
		String[] expectedTerminals = new String[]{"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[]{"S -> AB | .", "A -> AB | CB | a", "B -> AB | b", "C -> AC | c"};
		
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
		String[] expectedTerminals = new String[] {"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> AB | .", "A -> AB | CB | a", "B -> AB | b", "C -> AC | c"};
		
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
		String[] expectedTerminals = new String[] {"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> AB | .", "A -> AB | CB | a", "B -> AB | b", "C -> AC | c"};
		
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
		String[] expectedTerminals = new String[] {"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> AB | .", "A -> AB | CB | a", "B -> AB | b", "C -> AC | c"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		System.out.println("______________");
		for (Rule element : newG.getRules()) {
			System.out.println(element.getLeftSide() + "->" + element.getRightSide());
		}
		
		System.out.println(newG.getInitialSymbol());
		System.out.println(newG.getTerminals());
		System.out.println(newG.getVariables());
		
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));			
	}
	
	
	@Test
	public void testNoReach() {
		Grammar newG = GrammarParser.getGrammarWithoutNoReach(this.g);
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "c", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> AB | .", "A -> AB | CB | a", "B -> AB | b", "C -> AC | c"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));
	}
}
