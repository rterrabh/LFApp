package br.com.lfaplataform.test.withoutemptyproductions;

import static org.junit.Assert.*;
import junit.framework.*;
import br.com.lfaplataform.vo.*;

import org.junit.Test;

public class GrammarEssentiallyNoncontracting01 extends TestCase{

	private Grammar g;
	/*
	 Grammar:
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
	public void test() {
		this.g = GrammarParser.getGrammarEssentiallyNoncontracting(this.g);
		
		boolean test = false;
		for (Rule element : g.getRule()) {
			if (!element.getleftSide().equals(this.g.getInitialSymbol())) {
				assertTrue(!element.getrightSide().contains("."));
			} else {
				if (element.getrightSide().equals("."))
					test = true;
			}
		}
		
		assertTrue(test);
	}
	
}
