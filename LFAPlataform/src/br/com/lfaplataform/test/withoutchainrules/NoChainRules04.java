package br.com.lfaplataform.test.withoutchainrules;

import static org.junit.Assert.*;
import junit.framework.*;
import br.com.lfaplataform.vo.*;

import org.junit.Test;

public class NoChainRules04 extends TestCase{

	private Grammar g;
	/*
	 Grammar:
	 	S -> AB | A | B
	 	A -> aA | a
	 	B -> bB | A
	 */
	
	@Override
	protected void setUp() throws Exception {
		String[] variables = new String[] {"S", "A", "B"} ;
		String[] terminals = new String[] {"a", "b"};
		String initialSymbol = "S";
		String[] rules = new String[] {"S -> AB | A | B", "A -> aA | a",
				"B -> bB | A" };
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}

	@Test
	public void test() {
		this.g = GrammarParser.getGrammarWithoutChainRules(this.g);
		
		for (Rule element : g.getRule()) {
			if (element.getrightSide().length() == 1 && Character.isAlphabetic(element.getrightSide().charAt(0))) {
				assertFalse(Character.isUpperCase(element.getrightSide().charAt(0)));
			}				
		}
	}
	
}
