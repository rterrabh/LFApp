package br.com.lfaplataform.test.withoutinitialrecursion;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.*;
import br.com.lfaplataform.vo.*;


public class TestGrammar_InitialSymbolNotRecursive01 extends TestCase {

	private Grammar g;
	
	/*	GRAMMAR:
	    S -> aS | AB | AC
		A -> aA | .
		B -> bB | bS
		C -> cC | .
	 */
	
	@Override
	protected void setUp() throws Exception {
		String[] variables = new String[] {"S", "A", "B", "C"} ;
		String[] terminals = new String[] {"a", "b", "c"};
		String initialSymbol = "S";
		String[] rules = new String[] {"S -> aS | AB | AC", "A -> aA | .", "B -> bB | bS", "C -> cC | ." };
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}	
	
	@Test
	public void testInitialSymbolNotRecursive() {
		this.g = GrammarParser.getGrammarWithInitialSymbolNotRecursive(this.g);
		
		assertEquals("S'", this.g.getInitialSymbol());
		
		boolean test = false;
		for (Rule element : g.getRule()) {
			if (element.getleftSide().equals("S'") && element.getrightSide().equals("S"))
				test = true;
		}		
		assertTrue(test);	
	}
}
