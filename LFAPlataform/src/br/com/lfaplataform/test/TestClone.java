package br.com.lfaplataform.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import br.com.lfaplataform.vo.Grammar;
import br.com.lfaplataform.vo.Rule;

public class TestClone extends TestCase {

	private Grammar g;

	@Override
	protected void setUp() throws Exception {
		String variables[] = new String[] { "S", "S1", "A", "B", "V" };
		String terminals[] = new String[] { "a", "b" };
		String initialSymbol = "S";
		String rules[] = new String[] { "S -> AB | BS1 | BV", "S1 -> AB",
				"A -> BA | a", "B -> aa | VS1 | S1V | S1S1 | b", "V -> a" };
		this.g = new Grammar(variables, terminals, initialSymbol, rules);
	}

	/*
	@Override
	protected void tearDown() throws Exception {
		Grammar g1 = (Grammar) this.g.clone();
		
		
		//tirar um el de g1 e ver se ele continua em g
		
	}
	*/

	

}
