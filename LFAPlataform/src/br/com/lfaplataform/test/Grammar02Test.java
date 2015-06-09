package br.com.lfaplataform.test;

import static org.junit.Assert.*;

import java.util.Set;

import javax.swing.text.html.parser.Element;

import junit.framework.*;
import br.com.lfaplataform.vo.*;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

public class Grammar02Test extends TestCase {

	private Grammar g;
	
	/*
	 Grammar:
	 S -> aS | AB | AC
	 A -> aA | .
	 B -> bB | b
	 C -> cC | .
	 */
	
	@Override
	protected void setUp() throws Exception {
		String[] variables = new String[] {"S", "A", "B", "C"};
		String[] terminals = new String[] {"a", "b", "c"};
		String initialSymbol = "S";
		String[] rules = new String[] {"S -> aS | AB | AC", " A -> aA | .", "B -> bB | b", "C -> cC | ."};
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}
	
	
	
	@Test
	public void testInitialSymbolNotRecursive() {
		
		Grammar newG = GrammarParser.getGrammarWithInitialSymbolNotRecursive(this.g);
		
		String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {"S' -> S", "S -> aS | AB | AC", " A -> aA | .", "B -> bB | b", "C -> cC | ."};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		System.out.println(newG.getInitialSymbol());
		System.out.println(newG.getTerminals());
		System.out.println(newG.getVariables());
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));		
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));	
	}
	
	@Test
	public void testGrammarEssentiallyNonContracting() {
		
		Grammar newG = GrammarParser.getGrammarEssentiallyNoncontracting(this.g);
		
		String[] expectedVariables = new String[] {"S", "A", "B", "C"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> aS | AB | AC | a | B | A | C | .", " A -> aA | a", "B -> bB | b", "C -> cC | c"};
		
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
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> aS | AB | AC", " A -> aA | .", "B -> bB | b", "C -> cC | ."};
		
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
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> aS | AB | AC", " A -> aA | .", "B -> bB | b", "C -> cC | ."};
		
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
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S";
		String[] expectedRules = new String[] {"S -> aS | AB | AC", " A -> aA | .", "B -> bB | b", "C -> cC | ."};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));
	}
	
	@Test
	public void testFNC() {
		Grammar newG = GrammarParser.FNC(this.g);
		
		String[] expectedVariables = new String[] {"S'", "S", "A", "B", "C", "T1", "T2", "T3"};
		String[] expectedTerminals = new String[] {"a", "b", "c"};
		String expectedInitialSymbol = "S'";
		String[] expectedRules = new String[] {"S'-> T2B | T1S | T1A | c | T3C | AC | AB | . | b | a", 
				"S -> T2B | T1S | T1A | c | T3C | AC | AB | b | a", 
				"A -> a | T1A", "B -> T2B | b", "C -> T3C | c",
				"T1 -> a", "T2 -> b", "T3 -> c"};		
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(newG.getInitialSymbol(), expectedGrammar.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));	
		
	}
	
	@Test
	public void testFNG() {
		Grammar newG = GrammarParser.FNG(g);
		boolean fng = true;
		for (Rule element : newG.getRules()) {
			int counter = 0;
			if (!element.getLeftSide().equals(newG.getInitialSymbol()) && element.getRightSide().equals(".")) {
				fng = false;
				counter = 1;
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
		Set<String>[][] matrix = GrammarParser.CYK(g, "bbb");
		
		assertNotNull(matrix);
		assertNotNull(matrix[0][0]);
		assertNotEquals("", matrix[0][0]);
		
		Set<String> topVariables = matrix[0][0];
		
		assertEquals(0, topVariables.size());

		assertTrue(topVariables.isEmpty());
		//assertTrue(topVariables.contains("S"));
	}

}
