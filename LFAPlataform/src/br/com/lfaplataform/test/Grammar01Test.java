package br.com.lfaplataform.test;

import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import br.com.lfaplataform.vo.Grammar;
import br.com.lfaplataform.vo.GrammarParser;
import br.com.lfaplataform.vo.Rule;

public class Grammar01Test extends TestCase {

	private Grammar g;

	@Override
	protected void setUp() throws Exception {
		String variables[] = new String[] { "S", "A", "B", "C", "V" };
		String terminals[] = new String[] { "a", "b" };
		String initialSymbol = "S";
		String rules[] = new String[] { "S -> AB | BC | BV", "C -> AB",
				"A -> BA | a", "B -> aa | VC | CV | CC | b", "V -> a" };
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}

	@Override
	protected void tearDown() throws Exception {}	
	
	@Test
	public void testInitialSymbolNotRecursive() {
		
		Grammar newG = GrammarParser.getGrammarWithInitialSymbolNotRecursive(this.g);
		Grammar expectedGrammar = setGrammar();
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));	
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));	
	}

	@Test
	public void testGrammarEssentiallyNonContracting() {
		
		Grammar newG = GrammarParser.getGrammarEssentiallyNoncontracting(this.g);
		Grammar expectedGrammar = setGrammar();
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));		
	}
	
	@Test
	public void testChainRules() {
		
		Grammar newG = GrammarParser.getGrammarWithoutChainRules(this.g);
		Grammar expectedGrammar = setGrammar();
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));				
	}
	
	@Test
	public void testNoTerminals() {
		Grammar newG = GrammarParser.getGrammarWithoutNoTerm(this.g);
		Grammar expectedGrammar = setGrammar();
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));	
	}
	
	@Test
	public void testNoReach() {
		Grammar newG = GrammarParser.getGrammarWithoutNoReach(this.g);
		Grammar expectedGrammar = setGrammar();
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));	
	}
	
	
	@Test
	public void testFNC() {
		Grammar newG = GrammarParser.FNC(this.g);
		
		String[] expectedVariables = new String[] { "S", "A", "B", "C", "V" };
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> AB | BC | BV", "C -> AB",
				"A -> BA | a", "B -> T1T1 | VC | CV | CC | b", "V -> a", "T1 -> a" };
		
		System.out.println("______________");
		for (Rule element : newG.getRules()) {
			System.out.println(element.getLeftSide() + "->" + element.getRightSide());
		}
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);		
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));	
	}

	
	
	@Test
	public void testCYK() {
		Set<String>[][] matrix = GrammarParser.CYK(g, "bbabaa");
		
		assertNotNull(matrix);
		assertNotNull(matrix[0][0]);
		assertNotEquals("", matrix[0][0]);
		
		Set<String> topVariables = matrix[0][0];
		
		assertEquals(2, topVariables.size());

		assertTrue(topVariables.contains("A"));
		assertTrue(topVariables.contains("S"));
	}
	
	
	public Grammar setGrammar() {
		String[] expectedVariables = new String[] { "S", "A", "B", "C", "V" };
		String[] expectedTerminals = new String[] {"a", "b"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> AB | BC | BV", "C -> AB",
				"A -> BA | a", "B -> aa | VC | CV | CC | b", "V -> a" };
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		return expectedGrammar;
		
	}
	

}
