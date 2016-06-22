package vo;

import static org.junit.Assert.*;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import junit.framework.*;

public class Grammar08Test extends TestCase{

	private Grammar g;
	
	/*
	  A -> Aa | Aab | bb | b	 
	 */
	
	@Override
	protected void setUp() throws Exception {
		String[] variables = new String[]{"A"};
		String[] terminals = new String[]{"a", "b"};
		String initialSymbol = "A";
		String[] rules = new String[]{"A -> Aa | Aab | bb | b"};
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);	
	}
	
	@Test
	public void testRemovingTheImmediateLeftRecursion() {
		
		Grammar newG = GrammarParser.removingTheImmediateLeftRecursion(this.g);
		
		String[] expectedVariables = new String[]{"A","Z1"};
		String[] expectedTerminals = new String[]{"a", "b"};
		String expectedInitialSymbol = "A";
		String[] expectedRules = new String[]{"A -> bb | b | bbZ1 | bZ1", "Z1 -> aZ1 | abZ1 | a | ab"};
		
		Grammar expectedGrammar = new Grammar(expectedVariables, expectedTerminals, expectedInitialSymbol, expectedRules);		
		
		assertEquals(expectedGrammar.getInitialSymbol(), newG.getInitialSymbol());
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getTerminals(), newG.getTerminals()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getRules(), newG.getRules()));
		
		assertEquals(true, CollectionUtils.isEqualCollection(expectedGrammar.getVariables(), newG.getVariables()));
	}	

}
