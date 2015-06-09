package br.com.lfaplataform.test;

import static org.junit.Assert.*;
import br.com.lfaplataform.vo.*;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import junit.framework.*;

public class Grammar09Test extends TestCase{

	private Grammar g;
	
	/*
	  A -> Aa | Ab | b | c	 
	 */
	
	@Override
	protected void setUp() throws Exception {
		String[] variables = new String[]{"A"};
		String[] terminals = new String[]{"a", "b", "c"};
		String initialSymbol = "A";
		String[] rules = new String[]{"A -> Aa | Ab | b | c"};
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);	
	}
	
	@Test
	public void testRemovingTheImmediateLeftRecursion() {
		
		Grammar newG = GrammarParser.removingTheImmediateLeftRecursion(this.g);
		
		String[] expectedVariables = new String[]{"A","Z1"};
		String[] expectedTerminals = new String[]{"a", "b", "c"};
		String expectedInitialSymbol = "A";
		String[] expectedRules = new String[]{"A -> bZ1 | cZ1 | b | c", "Z1 -> aZ1 | bZ1 | a | b"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);		
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));
	}	

}
