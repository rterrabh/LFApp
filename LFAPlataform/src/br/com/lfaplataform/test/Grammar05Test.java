package br.com.lfaplataform.test;

import static org.junit.Assert.*;
import junit.framework.*;
import br.com.lfaplataform.vo.*;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

public class Grammar05Test extends TestCase{
	
	private Grammar g;
	
	/*
	S -> AC | BS | B
	A -> aA | aF
	B -> CF | b 
	C -> cC | c
	D -> aD | BD | C
	E -> aA | BSA
	F -> bB | b
	*/
	
	@Override
	protected void setUp() throws Exception {		
		String[] variables = new String[]{"S", "A", "B", "C", "D", "E", "F"};
		String[] terminals = new String[]{"a", "b", "c"};
		String initialSymbol = "S";
		String[] rules = new String[]{"S -> AC | BS | B", "A -> aA | aF", "B -> CF | b", "C -> cC | c", "D -> aD | BD | C", "E -> aA | BSA", "F -> bB | b"};
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);		
	}
	
	@Test
	public void testInitialSymbolNotRecursive() {
		
		Grammar newG = GrammarParser.getGrammarWithInitialSymbolNotRecursive(this.g);
		
		String[] expectedVariables = new String[]{"S'", "S", "A", "B", "C", "D", "E", "F"};
		String[] expectedTerminals = new String[]{"a", "b", "c"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[]{"S' -> S", "S -> AC | BS | B", "A -> aA | aF", "B -> CF | b", "C -> cC | c", "D -> aD | BD | C", "E -> aA | BSA", "F -> bB | b"};
		
		System.out.println();
		for (Rule element : newG.getRules()) {
			System.out.println(element.getLeftSide()+ "->" + element.getRightSide());
		}
		
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));
	}

	@Test
	public void testGrammarEssentiallyNonContracting() {
		
		Grammar newG = GrammarParser.getGrammarEssentiallyNoncontracting(this.g);
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C", "D", "E", "F"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> AC | BS | B", "A -> aA | aF", "B -> CF | b", "C -> cC | c", "D -> aD | BD | C", "E -> aA | BSA", "F -> bB | b"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
	
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));		
	}

	@Test
	public void testChainRules() {
		Grammar newG = GrammarParser.getGrammarWithoutChainRules(this.g);
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C", "D", "E", "F"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> AC | BS | CF | b", "A -> aA | aF", "B -> CF | b", "C -> cC | c", "D -> aD | BD | cC | c", "E -> aA | BSA", "F -> bB | b"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));		
	}
	
	@Test
	public void  testNoTerminals() {
		Grammar newG = GrammarParser.getGrammarWithoutNoTerm(this.g);
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C", "D", "E", "F"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> AC | BS | B", "A -> aA | aF", "B -> CF | b", "C -> cC | c", "D -> aD | BD | C", "E -> aA | BSA", "F -> bB | b"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));			
	}
	
}
