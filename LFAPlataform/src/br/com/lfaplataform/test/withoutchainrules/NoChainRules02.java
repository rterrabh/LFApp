package br.com.lfaplataform.test.withoutchainrules;

import static org.junit.Assert.*;
import junit.framework.*;
import br.com.lfaplataform.vo.*;

import org.junit.Test;

public class NoChainRules02 extends TestCase{

	private Grammar g;
	/*
	 Grammar:
	 	S -> ACA | CA | AA | AC | A | C | .
	 	A -> aAa | aa | B | C
	 	B -> bB | b
	 	C -> cC | c
	 */
	
	@Override
	protected void setUp() throws Exception {
		String[] variables = new String[] {"S", "A", "B", "C"} ;
		String[] terminals = new String[] {"a", "b", "c"};
		String initialSymbol = "S";
		String[] rules = new String[] {"S -> ACA | CA | AA | AC | A | C | .", "A -> aAa | aa | B | C",
				"B -> bB | b", "C -> cC | c" };
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}

	@Test
	public void test() {
		this.g = GrammarParser.getGrammarWithoutChainRules(this.g);
		
		for (Rule element : g.getRule()) {
			if (element.getrightSide().length() == 1) {
				assertFalse(Character.isUpperCase(element.getrightSide().charAt(0)));
			}				
		}
	}
	
}
