package br.com.lfaplataform.test.noterm;



import static org.junit.Assert.*;
import junit.framework.*;
import br.com.lfaplataform.vo.*;

import org.junit.Test;

public class WithoutNoTerm01 extends TestCase{

	private Grammar g;
	/*
	 Grammar:
	 	S -> AC | BS | B
	 	A -> aA | aF
	 	B -> CF | b
	 	C -> cC | D
	 	D -> aD | BD | C
	 	E -> aA | BSA
	 	F -> bB | b
	 */
	
	@Override
	protected void setUp() throws Exception {
		String[] variables = new String[] {"S", "A", "B", "C"} ;
		String[] terminals = new String[] {"a", "b", "c"};
		String initialSymbol = "S";
		String[] rules = new String[] {"S -> AC | BS | B", "A -> aA | aF", "B -> CF | b", "C -> cC | D",
				"D -> aD | BD | C", "E -> aA | BSA", "F -> bB | b"};
		
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}

	@Test
	public void test() {
		this.g = GrammarParser.getGrammarWithoutNoTerm(this.g);
		
		for (Rule element : g.getRule()) {
			if (element.getrightSide().length() == 1) {
				assertFalse(Character.isUpperCase(element.getrightSide().charAt(0)));
			}				
		}
	}
	
}
