package br.com.lfaplataform.test.withoutemptyproductions;

import static org.junit.Assert.*;
import junit.framework.*;
import br.com.lfaplataform.vo.*;

import org.junit.Test;

public class GrammarEssentiallyNoncontracting02 extends TestCase{

	private Grammar g;
	/*
	 Grammar:
	 	S -> aAb
		A -> aA | B
		B -> bB | .
	 */
	
	@Override
	protected void setUp() throws Exception {
		String[] variables = new String[] {"S", "A", "B"} ;
		String[] terminals = new String[] {"a", "b"};
		String initialSymbol = "S";
		String[] rules = new String[] {"S -> aAb", "A -> aA | B", "B -> bB | ."};
		
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