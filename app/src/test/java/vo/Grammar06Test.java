package vo;

import static org.junit.Assert.*;
import junit.framework.*;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

public class Grammar06Test extends TestCase {
	
	private Grammar g;

	/*
	S -> AB | .
	A -> AB | CB | a
	B -> AB | b
	C -> AC | c 	 
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
		
		Grammar newG = g.getGrammarWithInitialSymbolNotRecursive(this.g, new AcademicSupport());
		
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
		
		Grammar newG = g.getGrammarEssentiallyNoncontracting(this.g, new AcademicSupport());
		
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
		Grammar newG = g.getGrammarWithoutChainRules(this.g, new AcademicSupport());
		
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
		Grammar newG = g.getGrammarWithoutNoTerm(this.g, new AcademicSupport());
		
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
	public void testNoReach() {
		Grammar newG = g.getGrammarWithoutNoReach(this.g, new AcademicSupport());
		
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
	public void testFNG() {
		Grammar newG = g.FNG(g, new AcademicSupport());
		boolean fng = true;
		for (Rule element : newG.getRules()) {
			int counter = 0;
			if (!element.getLeftSide().equals(newG.getInitialSymbol()) && element.getRightSide().equals("")) {
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
}