package br.com.lfaplataform.test.withoutinitialrecursion;

import static org.junit.Assert.*;
import junit.framework.*;
import br.com.lfaplataform.vo.*;

import org.junit.Test;

public class TestGrammar_InitialSymbolNotRecursive02 extends TestCase {

	private Grammar g;

	/* 
	 Grammar:
	 S -> ABC | f
	 A -> aA | a
	 B -> Sb
	 C -> cdC | dC | e	  
	 */
	
	
	@Override
	protected void setUp() throws Exception {
		String[] variables = new String[] {"S", "A", "B", "C"} ;
		String[] terminals = new String[] {"a", "b", "c", "d", "e", "f"};
		String initialSymbol = "S";
		String[] rules = new String[] {"S -> ABC | f", "A -> aA | a", "B -> Sb", "C -> cdC | dC | e" };
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
		
	}
	
	@Test
	public void test() {
		this.g = GrammarParser.getGrammarWithInitialSymbolNotRecursive(this.g);
		assertEquals("S", this.g.getInitialSymbol());
		
		boolean test = false;
		for (Rule element : g.getRule()) {
			if (element.getleftSide().equals("S'") && element.getrightSide().equals("S"))
				test = true;
		}		
		assertFalse(test);			
	}

}
