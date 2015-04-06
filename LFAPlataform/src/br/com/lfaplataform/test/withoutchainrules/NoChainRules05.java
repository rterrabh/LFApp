package br.com.lfaplataform.test.withoutchainrules;

import static org.junit.Assert.*;
import junit.framework.*;
import br.com.lfaplataform.vo.*;

import org.junit.Test;

public class NoChainRules05 extends TestCase{

	private Grammar g;
	/*
	 Grammar:
	 	S -> ABC | AB | BC | AC | A | B | C | .
	 	A -> aA | a
	 	B -> bB | b
	 	C -> cC | c
	 */
	
	@Override
	protected void setUp() throws Exception {
		String[] variables = new String[] {"S", "A", "B", "C"} ;
		String[] terminals = new String[] {"a", "b", "c"};
		String initialSymbol = "S";
		String[] rules = new String[] {"S -> ABC | AB | BC | AC | A | B | C | .", "A -> aA | a",
				"B -> bB | b", "C -> cC | c" };
		
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
