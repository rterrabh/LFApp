package br.com.lfaplataform.test.withoutinitialrecursion;

import static org.junit.Assert.*;
import junit.framework.*;

import org.junit.Test;

import br.com.lfaplataform.vo.Grammar;
import br.com.lfaplataform.vo.GrammarParser;
import br.com.lfaplataform.vo.Rule;

public class TestGrammar_InitialSymbolNotRecursive04 extends TestCase {

	private Grammar g;
	/* Grammar:
	 S -> ABC
	 A -> BB | .
	 B -> CC | a
	 C -> AA | b
	 */
	
	@Override
	protected void setUp() throws Exception {
		String[] variables = new String[] {"S", "A", "B", "C"} ;
		String[] terminals = new String[] {"a", "b"};
		String initialSymbol = "S";
		String[] rules = new String[] {"S -> ABC", "A -> BB | .", "B -> CC | a", "C -> AA | b" };
		
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
